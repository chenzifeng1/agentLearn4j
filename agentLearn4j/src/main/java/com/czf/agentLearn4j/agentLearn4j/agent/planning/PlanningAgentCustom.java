package com.czf.agentLearn4j.agentLearn4j.agent.planning;

import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.internal.node.Node;
import com.czf.agentLearn4j.agentLearn4j.agent.core.CustomBaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Planning Agent
 * Creates and executes multi-step plans
 */
@Slf4j
@Component
public class PlanningAgentCustom extends CustomBaseAgent {

    private final TaskPlanner taskPlanner;
    private final PlanExecutor planExecutor;

    /**
     * 使用简化构造方法
     */
    public PlanningAgentCustom(TaskPlanner taskPlanner, PlanExecutor planExecutor) {
        super("PlanningAgent", "Multi-step task planning and execution agent");
        this.taskPlanner = taskPlanner;
        this.planExecutor = planExecutor;
    }

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_PLANNING;
    }

    @Override
    public String execute(String input) {
        // Create plan
        String plan = taskPlanner.createPlan(input);

        // Execute plan
        return planExecutor.executePlan(plan);
    }

    @Override
    public Node asNode(boolean includeContents, boolean returnReasoningContents) {
        return null;
    }

    @Override
    protected StateGraph initGraph() throws GraphStateException {
        return null;
    }
}
