package com.czf.agentLearn4j.agentLearn4j.agent.react;

import com.czf.agentLearn4j.agentLearn4j.agent.core.CustomBaseAgent;
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
public class ReactAgentCustom extends CustomBaseAgent {

    private final ReasoningEngine reasoningEngine;
    private final ActionSelector actionSelector;
    private final ObservationProcessor observationProcessor;

    /**
     * 使用简化构造方法
     */
    public ReactAgentCustom(ReasoningEngine reasoningEngine, ActionSelector actionSelector,
                           ObservationProcessor observationProcessor) {
        super("ReactAgent", "Reasoning and Acting agent with iterative problem-solving");
        this.reasoningEngine = reasoningEngine;
        this.actionSelector = actionSelector;
        this.observationProcessor = observationProcessor;
    }

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
