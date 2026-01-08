package com.czf.agentLearn4j.agentLearn4j.agent.react;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Observation Processor
 * Processes results of actions
 */
@Slf4j
@Component
public class ObservationProcessor {

    /**
     * Process action result
     */
    public String process(String action) {
        log.debug("Processing observation for action: {}", action);
        return "Observed result from: " + action;
    }

}
