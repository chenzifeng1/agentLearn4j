package com.czf.agentLearn4j.agentLearn4j.agent.react;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ReAct Agent
 * Implements Reasoning and Acting pattern
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReactAgent extends BaseAgent {

    private final ReasoningEngine reasoningEngine;
    private final ActionSelector actionSelector;
    private final ObservationProcessor observationProcessor;

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_REACT;
    }

    @Override
    public String execute(String input) {
        String thought = reasoningEngine.reason(input);
        String action = actionSelector.selectAction(thought);
        String observation = observationProcessor.process(action);

        return String.format("Thought: %s\nAction: %s\nObservation: %s",
                            thought, action, observation);
    }

}
