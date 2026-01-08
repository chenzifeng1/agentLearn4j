package com.czf.agentLearn4j.agentLearn4j.common.exception;

/**
 * Base exception for agent-related errors
 */
public class AgentException extends RuntimeException {

    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }

}
