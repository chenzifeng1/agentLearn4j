package com.czf.agentLearn4j.agentLearn4j.common.exception;

/**
 * Exception thrown when an agent is not found
 */
public class AgentNotFoundException extends AgentException {

    public AgentNotFoundException(String message) {
        super(message);
    }

    public AgentNotFoundException(String agentType, String message) {
        super("Agent not found: " + agentType);
    }

}
