package com.czf.agentLearn4j.agentLearn4j.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 OpenAI 兼容的 ChatModel
 * 直接使用 RestClient 发送 OpenAI 标准格式的请求
 *
 * @Primary 标记为主要的 ChatModel Bean，优先于 DashScope 的自动配置
 */
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class CustomOpenAiCompatibleChatModel implements ChatModel {

    private static final String API_URL = "https://openapi-ait.ke.com/v1/chat/completions";
    private static final String API_KEY = "yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI";
    private static final String MODEL = "gpt-5-chat";

    private final RestClient restClient;

    public CustomOpenAiCompatibleChatModel() {
        // 创建带日志的 RestClient
        this.restClient = RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    log.info("========== HTTP Request ==========");
                    log.info("URI: {}", request.getURI());
                    log.info("Method: {}", request.getMethod());
                    log.info("Headers: {}", request.getHeaders());
                    if (body != null && body.length > 0) {
                        log.info("Request Body: {}", new String(body));
                    }

                    var response = execution.execute(request, body);

                    log.info("========== HTTP Response ==========");
                    log.info("Status Code: {}", response.getStatusCode());
                    log.info("Response Headers: {}", response.getHeaders());

                    return response;
                })
                .build();
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        try {
            // 构建 OpenAI 格式的请求
            OpenAiRequest request = buildRequest(prompt);

            log.info("发送 OpenAI 格式请求");

            // 发送请求
            OpenAiResponse response = restClient.post()
                    .uri(API_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(OpenAiResponse.class);

            log.info("Response Body: {}", response);

            // 转换为 Spring AI 的 ChatResponse
            return convertToChatResponse(response);

        } catch (Exception e) {
            log.error("调用 OpenAI 兼容接口失败", e);
            throw new RuntimeException("调用 AI 模型失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建 OpenAI 格式的请求
     */
    private OpenAiRequest buildRequest(Prompt prompt) {
        OpenAiRequest request = new OpenAiRequest();
        request.setModel(MODEL);
        request.setStream(false);
        request.setTemperature(0.7);
        request.setMaxTokens(2000);

        // 转换消息格式
        List<OpenAiMessage> messages = prompt.getInstructions().stream()
                .map(this::convertToOpenAiMessage)
                .collect(Collectors.toList());
        request.setMessages(messages);

        return request;
    }

    /**
     * 转换消息格式
     */
    private OpenAiMessage convertToOpenAiMessage(Message message) {
        OpenAiMessage openAiMessage = new OpenAiMessage();

        if (message instanceof UserMessage) {
            openAiMessage.setRole("user");
            openAiMessage.setContent(message.getText());
        } else if (message instanceof AssistantMessage) {
            openAiMessage.setRole("assistant");
            openAiMessage.setContent(message.getText());
        } else {
            openAiMessage.setRole("user");
            openAiMessage.setContent(message.getText());
        }

        return openAiMessage;
    }

    /**
     * 转换为 Spring AI 的 ChatResponse
     */
    private ChatResponse convertToChatResponse(OpenAiResponse response) {
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("AI 模型返回空响应");
        }

        OpenAiChoice firstChoice = response.getChoices().get(0);
        String content = firstChoice.getMessage().getContent();

        // 创建 AssistantMessage
        AssistantMessage assistantMessage = new AssistantMessage(content);

        // 创建 Generation
        Generation generation = new Generation(assistantMessage);

        // 创建 ChatResponse
        return new ChatResponse(List.of(generation));
    }

    /**
     * OpenAI 请求格式
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpenAiRequest {
        private String model;
        private List<OpenAiMessage> messages;
        private Boolean stream;
        private Double temperature;
        @JsonProperty("max_tokens")
        private Integer maxTokens;
    }

    /**
     * OpenAI 消息格式
     */
    @Data
    public static class OpenAiMessage {
        private String role;
        private String content;
    }

    /**
     * OpenAI 响应格式
     */
    @Data
    public static class OpenAiResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<OpenAiChoice> choices;
        private OpenAiUsage usage;
    }

    /**
     * OpenAI Choice
     */
    @Data
    public static class OpenAiChoice {
        private Integer index;
        private OpenAiMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    /**
     * OpenAI Usage
     */
    @Data
    public static class OpenAiUsage {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
