package com.czf.agentLearn4j.agentLearn4j.agent.core;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract Base Agent
 * Foundation class for all agent implementations
 */
@Slf4j
public abstract class BaseAgent {

    /**
     * Get agent type identifier
     */
    public abstract String getAgentType();

    /**
     * Execute agent logic
     * @param input User input
     * @return Agent output
     */
    public abstract String execute(String input);

    /**
     * Validate input before execution
     * @param input User input
     * @return true if valid
     */
    protected boolean validateInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Pre-execution hook
     */
    protected void beforeExecute(String input) {
        log.debug("Agent {} executing with input: {}", getAgentType(), input);
    }

    /**
     * Post-execution hook
     */
    protected void afterExecute(String output) {
        log.debug("Agent {} completed with output: {}", getAgentType(), output);
    }

}
