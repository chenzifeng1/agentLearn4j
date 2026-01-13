package com.czf.agentLearn4j.agentLearn4j.agent.workflow.node;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * Intent Routing Node
 * 意图路由节点 - 根据识别的意图路由到不同的处理分支
 */
@Slf4j
@Component
public class IntentRoutingNode implements Function<Map<String, Object>, Map<String, Object>> {

    /**
     * 执行意图路由逻辑
     * @param state 当前状态，包含识别的意图
     * @return 更新后的状态，包含路由决策
     */
    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        log.info("执行意图路由节点");

        String intent = (String) state.getOrDefault("intent", "UNKNOWN");
        Double confidence = (Double) state.getOrDefault("confidence", 0.0);

        // 如果置信度太低，路由到默认处理器
        if (confidence < 0.6) {
            log.warn("意图识别置信度过低: {}, 路由到默认处理器", confidence);
            state.put("route", "default");
            state.put("routeReason", "置信度过低");
            return state;
        }

        // 根据意图类型决定路由
        String route = routeByIntent(intent);
        state.put("route", route);
        state.put("routeReason", "根据意图 " + intent + " 路由");

        log.info("意图 {} 路由到: {}", intent, route);

        return state;
    }

    /**
     * 根据意图类型决定路由目标
     */
    private String routeByIntent(String intent) {
        return switch (intent.toUpperCase()) {
            case "QUERY" -> "knowledge_query";    // 路由到知识查询处理器
            case "CHAT" -> "conversational";      // 路由到对话处理器
            case "TASK" -> "task_execution";      // 路由到任务执行处理器
            case "TOOL" -> "tool_calling";        // 路由到工具调用处理器
            case "PLANNING" -> "planning";        // 路由到规划处理器
            default -> "default";                 // 默认处理器
        };
    }

    /**
     * 判断是否应该路由到特定分支（用于 StateGraph 的条件分支）
     * 这个方法可以作为 StateGraph.addConditionalEdges 的条件函数
     */
    public static String getNextNode(Map<String, Object> state) {
        String route = (String) state.getOrDefault("route", "default");
        return route;
    }
}
