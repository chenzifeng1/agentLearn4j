package com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * Query Handler Node
 * 查询处理节点 - 处理知识查询类型的请求
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QueryHandlerNode implements Function<Map<String, Object>, Map<String, Object>> {

    private final ChatModel chatModel;

    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        log.info("执行查询处理节点");

        String input = (String) state.get("input");

        try {
            // 构建查询提示词
            String prompt = String.format("""
                你是一个专业的知识助手，请根据用户的查询提供准确的信息。

                用户查询：%s

                请提供详细且准确的回答。
                """, input);

            // 调用 AI 模型
            ChatResponse response = chatModel.call(new Prompt(prompt));
            String result = response.getResult().getOutput().getText();

            state.put("output", result);
            state.put("processingNode", "query_handler");

            log.info("查询处理完成");

        } catch (Exception e) {
            log.error("查询处理失败", e);
            state.put("output", "抱歉，查询处理失败：" + e.getMessage());
            state.put("error", e.getMessage());
        }

        return state;
    }
}
