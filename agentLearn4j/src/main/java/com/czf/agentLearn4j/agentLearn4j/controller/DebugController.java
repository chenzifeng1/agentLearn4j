package com.czf.agentLearn4j.agentLearn4j.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Debug Controller
 * 用于测试和诊断 AI 模型调用问题
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/debug")
@RequiredArgsConstructor
public class DebugController {

    private final ChatModel chatModel;

    /**
     * 简单测试接口 - 直接调用 ChatModel
     */
    @PostMapping("/simple-test")
    public Map<String, Object> simpleTest(@RequestBody Map<String, String> request) {
        log.info("========== Debug Simple Test Start ==========");
        log.info("Received request: {}", request);

        String input = request.getOrDefault("input", "你好");

        try {
            log.info("Calling ChatModel with input: {}", input);

            Prompt prompt = new Prompt(input);
            log.info("Created Prompt: {}", prompt);

            ChatResponse response = chatModel.call(prompt);
            log.info("Received ChatResponse: {}", response);

            String output = response.getResult().getOutput().getText();
            log.info("Extracted output text: {}", output);

            log.info("========== Debug Simple Test Success ==========");

            return Map.of(
                "success", true,
                "input", input,
                "output", output,
                "metadata", response.getMetadata() != null ? response.getMetadata().toString() : "null"
            );

        } catch (Exception e) {
            log.error("========== Debug Simple Test Failed ==========", e);
            log.error("Error type: {}", e.getClass().getName());
            log.error("Error message: {}", e.getMessage());

            return Map.of(
                "success", false,
                "input", input,
                "error", e.getMessage(),
                "errorType", e.getClass().getName()
            );
        }
    }

    /**
     * 测试配置信息
     */
    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        return Map.of(
            "message", "Check application logs for DashScopeApi configuration",
            "note", "Model configuration is logged at startup"
        );
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "ok",
            "service", "Debug Controller"
        );
    }
}
