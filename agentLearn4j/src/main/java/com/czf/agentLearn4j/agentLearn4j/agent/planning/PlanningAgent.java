package com.czf.agentLearn4j.agentLearn4j.agent.planning;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
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
@RequiredArgsConstructor
public class PlanningAgent extends BaseAgent {

    private final TaskPlanner taskPlanner;
    private final PlanExecutor planExecutor;

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

}
