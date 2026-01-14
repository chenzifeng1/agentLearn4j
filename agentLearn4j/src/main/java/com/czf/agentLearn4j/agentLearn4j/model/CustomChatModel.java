package com.czf.agentLearn4j.agentLearn4j.model;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.model.ChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 类名：CustomModel
 * 描述：自定义 ChatModel 配置，包含详细的请求/响应日志
 * 作者：zifengchen
 * 日期：2026/1/13
 */
@Slf4j
@Component
public class CustomChatModel {

    // 注释掉 DashScope ChatModel，使用自定义的 OpenAI 兼容 ChatModel
    // @Bean
    // public DashScopeChatModel normalChatModel() {
    //     return DashScopeChatModel.builder()
    //             .dashScopeApi(getDashScopeApi())
    //             .defaultOptions(DashScopeChatOptions.builder()
    //                     .withModel("gpt-5-chat")  // 使用代理接口的模型名称
    //                     .withTemperature(0.7)     // 控制随机性
    //                     .withMaxToken(2000)       // 最大输出长度
    //                     .withTopP(0.9)            // 核采样参数
    //                     .build())
    //             .build();
    // }

    DashScopeApi getDashScopeApi() {
        // 创建带有详细日志的 RestClient
        RestClient.Builder restClientBuilder = RestClient.builder()
                .requestInterceptor(loggingInterceptor());

        return DashScopeApi.builder()
                .baseUrl("https://openapi-ait.ke.com")
                .completionsPath("/v1/chat/completions")
                .apiKey("yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI")
                .restClientBuilder(restClientBuilder)
                .build();
    }

    /**
     * 创建请求/响应日志拦截器
     */
    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            log.info("========== HTTP Request ==========");
            log.info("URI: {}", request.getURI());
            log.info("Method: {}", request.getMethod());
            log.info("Headers: {}", request.getHeaders());

            // 记录请求体
            if (body != null && body.length > 0) {
                String requestBody = new String(body, StandardCharsets.UTF_8);
                log.info("Request Body: {}", requestBody);
            }

            // 执行请求
            var response = execution.execute(request, body);

            // 记录响应
            log.info("========== HTTP Response ==========");
            log.info("Status Code: {}", response.getStatusCode());
            log.info("Response Headers: {}", response.getHeaders());

            // 读取响应体（注意：这可能会消耗流，所以需要小心处理）
            try {
                if (response.getBody() != null) {
                    String responseBody = new BufferedReader(
                        new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                    log.info("Response Body: {}", responseBody);
                }
            } catch (Exception e) {
                log.warn("Failed to read response body: {}", e.getMessage());
            }
            log.info("===================================");

            return response;
        };
    }
}
