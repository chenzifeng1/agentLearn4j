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
 * Chat Handler Node
 * 对话处理节点 - 处理闲聊对话类型的请求
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandlerNode implements Function<Map<String, Object>, Map<String, Object>> {

    private final DashScopeChatModel chatModel;

    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        log.info("执行对话处理节点");

        String input = (String) state.get("input");

        try {
            // 构建对话提示词
            String prompt = String.format("""
                你是一个友好的对话助手，请以自然、轻松的方式与用户交流。

                用户说：%s

                请给出友好的回复。
                """, input);

            // 调用 AI 模型
            ChatResponse response = chatModel.call(new Prompt(prompt));
            String result = response.getResult().getOutput().getText();

            state.put("output", result);
            state.put("processingNode", "chat_handler");

            log.info("对话处理完成");

        } catch (Exception e) {
            log.error("对话处理失败", e);
            state.put("output", "抱歉，我现在无法回复：" + e.getMessage());
            state.put("error", e.getMessage());
        }

        return state;
    }
}
