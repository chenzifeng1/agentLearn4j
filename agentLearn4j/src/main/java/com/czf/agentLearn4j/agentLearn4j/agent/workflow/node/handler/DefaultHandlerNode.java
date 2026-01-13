package com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.handler;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * Default Handler Node
 * 默认处理节点 - 处理无法明确分类的请求
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultHandlerNode implements Function<Map<String, Object>, Map<String, Object>> {

    private final DashScopeChatModel chatModel;

    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        log.info("执行默认处理节点");

        String input = (String) state.get("input");

        try {
            // 使用通用的处理方式
            ChatResponse response = chatModel.call(new Prompt(input));
            String result = response.getResult().getOutput().getText();

            state.put("output", result);
            state.put("processingNode", "default_handler");

            log.info("默认处理完成");

        } catch (Exception e) {
            log.error("默认处理失败", e);
            state.put("output", "抱歉，处理失败：" + e.getMessage());
            state.put("error", e.getMessage());
        }

        return state;
    }
}
