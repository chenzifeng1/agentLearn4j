package com.czf.agentLearn4j.agentLearn4j.agent.workflow;

import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.internal.node.Node;
import com.czf.agentLearn4j.agentLearn4j.agent.core.CustomBaseAgent;
import com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.IntentRecognitionNode;
import com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.IntentRoutingNode;
import com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.handler.ChatHandlerNode;
import com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.handler.DefaultHandlerNode;
import com.czf.agentLearn4j.agentLearn4j.agent.workflow.node.handler.QueryHandlerNode;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Intent-Based Workflow Agent
 * 基于意图识别和路由的工作流 Agent
 */
@Slf4j
@Component
public class IntentBasedWorkflowAgent extends CustomBaseAgent {

    private final IntentRecognitionNode intentRecognitionNode;
    private final IntentRoutingNode intentRoutingNode;
    private final QueryHandlerNode queryHandlerNode;
    private final ChatHandlerNode chatHandlerNode;
    private final DefaultHandlerNode defaultHandlerNode;

    public IntentBasedWorkflowAgent(
            IntentRecognitionNode intentRecognitionNode,
            IntentRoutingNode intentRoutingNode,
            QueryHandlerNode queryHandlerNode,
            ChatHandlerNode chatHandlerNode,
            DefaultHandlerNode defaultHandlerNode) {
        super("IntentBasedWorkflowAgent", "工作流 Agent，支持意图识别和路由");
        this.intentRecognitionNode = intentRecognitionNode;
        this.intentRoutingNode = intentRoutingNode;
        this.queryHandlerNode = queryHandlerNode;
        this.chatHandlerNode = chatHandlerNode;
        this.defaultHandlerNode = defaultHandlerNode;
    }

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_WORKFLOW;
    }

    @Override
    public String execute(String input) {
        log.info("执行意图工作流，输入: {}", input);

        try {
            // 初始化状态
            Map<String, Object> state = new HashMap<>();
            state.put("input", input);

            // 执行工作流
            state = executeWorkflow(state);

            // 提取输出
            String output = (String) state.getOrDefault("output", "处理完成，但未生成输出");
            log.info("工作流执行完成，输出: {}", output);

            return output;

        } catch (Exception e) {
            log.error("工作流执行失败", e);
            return "工作流执行失败: " + e.getMessage();
        }
    }

    /**
     * 执行工作流的核心逻辑
     */
    private Map<String, Object> executeWorkflow(Map<String, Object> state) {
        // 1. 意图识别
        state = intentRecognitionNode.apply(state);

        // 2. 意图路由
        state = intentRoutingNode.apply(state);

        // 3. 根据路由执行对应的处理器
        String route = (String) state.get("route");
        state = executeHandler(state, route);

        return state;
    }

    /**
     * 支持流式执行的方法
     */
    public Flux<String> streamExecute(String input) {
        log.info("执行流式意图工作流，输入: {}", input);

        return Flux.create(sink -> {
            try {
                // 初始化状态
                Map<String, Object> state = new HashMap<>();
                state.put("input", input);

                // 发送开始事件
                sink.next("data: " + toJson("event", "start", "message", "工作流开始执行") + "\n\n");

                // 执行意图识别
                sink.next("data: " + toJson("event", "intent_recognition", "message", "正在识别意图...") + "\n\n");
                state = intentRecognitionNode.apply(state);
                String intent = (String) state.get("intent");
                sink.next("data: " + toJson("event", "intent_recognized", "intent", intent) + "\n\n");

                // 执行意图路由
                sink.next("data: " + toJson("event", "routing", "message", "正在路由请求...") + "\n\n");
                state = intentRoutingNode.apply(state);
                String route = (String) state.get("route");
                sink.next("data: " + toJson("event", "routed", "route", route) + "\n\n");

                // 根据路由执行对应的处理器
                sink.next("data: " + toJson("event", "processing", "message", "正在处理请求...") + "\n\n");
                state = executeHandler(state, route);

                // 发送最终结果
                String output = (String) state.getOrDefault("output", "处理完成");
                sink.next("data: " + toJson("event", "result", "output", output) + "\n\n");
                sink.next("data: " + toJson("event", "complete", "message", "工作流执行完成") + "\n\n");

                sink.complete();

            } catch (Exception e) {
                log.error("流式工作流执行失败", e);
                sink.next("data: " + toJson("event", "error", "message", e.getMessage()) + "\n\n");
                sink.error(e);
            }
        });
    }

    /**
     * 根据路由执行对应的处理器
     */
    private Map<String, Object> executeHandler(Map<String, Object> state, String route) {
        return switch (route) {
            case "knowledge_query" -> queryHandlerNode.apply(state);
            case "conversational" -> chatHandlerNode.apply(state);
            default -> defaultHandlerNode.apply(state);
        };
    }

    /**
     * 简单的 JSON 构造方法（实际项目中应该使用 Jackson）
     */
    private String toJson(String... keyValues) {
        StringBuilder json = new StringBuilder("{");
        for (int i = 0; i < keyValues.length; i += 2) {
            if (i > 0) json.append(",");
            json.append("\"").append(keyValues[i]).append("\":\"")
                .append(escapeJson(keyValues[i + 1])).append("\"");
        }
        json.append("}");
        return json.toString();
    }

    /**
     * 转义 JSON 字符串中的特殊字符
     */
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    @Override
    public Node asNode(boolean includeContents, boolean returnReasoningContents) {
        return null;
    }

    @Override
    protected StateGraph initGraph() throws GraphStateException {
        // 简化实现，不使用复杂的 StateGraph
        return null;
    }
}
