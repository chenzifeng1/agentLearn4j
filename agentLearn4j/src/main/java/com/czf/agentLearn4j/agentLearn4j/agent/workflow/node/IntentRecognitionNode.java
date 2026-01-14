package com.czf.agentLearn4j.agentLearn4j.agent.workflow.node;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * Intent Recognition Node
 * 意图识别节点 - 使用 AI 模型识别用户输入的意图
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IntentRecognitionNode implements Function<Map<String, Object>, Map<String, Object>> {

    private final ChatModel chatModel;

    /**
     * 执行意图识别
     * @param state 当前状态，包含用户输入
     * @return 更新后的状态，包含识别的意图
     */
    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        log.info("执行意图识别节点");

        String userInput = (String) state.get("input");
        if (userInput == null || userInput.trim().isEmpty()) {
            state.put("intent", "unknown");
            state.put("confidence", 0.0);
            return state;
        }

        try {
            // 构建意图识别提示词
            String intentPrompt = buildIntentPrompt(userInput);

            // 调用 AI 模型进行意图识别
            ChatResponse response = chatModel.call(new Prompt(intentPrompt));
            String aiResponse = response.getResult().getOutput().getText();

            // 解析 AI 响应，提取意图和置信度
            IntentResult intentResult = parseIntentResponse(aiResponse);

            state.put("intent", intentResult.getIntent());
            state.put("confidence", intentResult.getConfidence());
            state.put("intentDescription", intentResult.getDescription());

            log.info("识别的意图: {}, 置信度: {}", intentResult.getIntent(), intentResult.getConfidence());

        } catch (Exception e) {
            log.error("意图识别失败", e);
            state.put("intent", "error");
            state.put("confidence", 0.0);
            state.put("error", e.getMessage());
        }

        return state;
    }

    /**
     * 构建意图识别的提示词
     */
    private String buildIntentPrompt(String userInput) {
        return String.format("""
            你是一个专业的意图识别助手。请分析用户的输入，识别其意图类型。

            支持的意图类型：
            1. QUERY - 查询问题，需要知识检索或信息查询
            2. CHAT - 闲聊对话，日常交流
            3. TASK - 任务执行，需要执行某个具体操作
            4. TOOL - 工具调用，需要调用特定工具
            5. PLANNING - 规划类任务，需要制定计划
            6. UNKNOWN - 无法识别的意图

            用户输入：%s

            请以 JSON 格式返回结果：
            {
              "intent": "意图类型（大写）",
              "confidence": 置信度（0-1之间的小数）,
              "description": "简短的意图描述"
            }

            只返回 JSON，不要其他内容。
            """, userInput);
    }

    /**
     * 解析 AI 响应，提取意图信息
     */
    private IntentResult parseIntentResponse(String aiResponse) {
        try {
            // 简单的 JSON 解析（实际项目中应该使用 Jackson 或 Gson）
            String intent = extractJsonField(aiResponse, "intent");
            double confidence = Double.parseDouble(extractJsonField(aiResponse, "confidence"));
            String description = extractJsonField(aiResponse, "description");

            return new IntentResult(intent, confidence, description);
        } catch (Exception e) {
            log.warn("解析意图响应失败，使用默认值", e);
            return new IntentResult("UNKNOWN", 0.5, "解析失败");
        }
    }

    /**
     * 从 JSON 字符串中提取字段值
     */
    private String extractJsonField(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\"\\s*:\\s*\"?([^,}\"]+)\"?";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }

    /**
     * 意图识别结果
     */
    private static class IntentResult {
        private final String intent;
        private final double confidence;
        private final String description;

        public IntentResult(String intent, double confidence, String description) {
            this.intent = intent;
            this.confidence = confidence;
            this.description = description;
        }

        public String getIntent() {
            return intent;
        }

        public double getConfidence() {
            return confidence;
        }

        public String getDescription() {
            return description;
        }
    }
}
