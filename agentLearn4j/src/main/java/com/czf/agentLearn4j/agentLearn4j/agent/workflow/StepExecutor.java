package com.czf.agentLearn4j.agentLearn4j.agent.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Step Executor
 * Executes individual workflow steps
 */
@Slf4j
@Component
public class StepExecutor {

    /**
     * Execute step
     */
    public String executeStep(WorkflowDefinition.WorkflowStep step, String input) {
        log.debug("Executing step: {}", step.getName());
        return "Step " + step.getName() + " executed with: " + input;
    }

}
