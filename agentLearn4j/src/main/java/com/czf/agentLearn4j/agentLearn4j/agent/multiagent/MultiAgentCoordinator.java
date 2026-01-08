package com.czf.agentLearn4j.agentLearn4j.agent.multiagent;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Multi-Agent Coordinator
 * Orchestrates multiple agents working together
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MultiAgentCoordinator extends BaseAgent {

    private final TaskDistributor taskDistributor;
    private final ResultAggregator resultAggregator;

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_MULTIAGENT;
    }

    @Override
    public String execute(String input) {
        // Distribute task to multiple agents
        String distributed = taskDistributor.distribute(input);

        // Aggregate results
        return resultAggregator.aggregate(distributed);
    }

}
