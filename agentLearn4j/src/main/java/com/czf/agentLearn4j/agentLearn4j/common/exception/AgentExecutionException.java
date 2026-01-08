package com.czf.agentLearn4j.agentLearn4j.common.exception;

/**
 * Exception thrown when agent execution fails
 */
public class AgentExecutionException extends AgentException {

    public AgentExecutionException(String message) {
        super(message);
    }

    public AgentExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
