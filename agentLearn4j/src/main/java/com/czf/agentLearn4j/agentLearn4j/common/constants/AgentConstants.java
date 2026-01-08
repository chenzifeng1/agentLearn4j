package com.czf.agentLearn4j.agentLearn4j.common.constants;

/**
 * Agent-related Constants
 */
public final class AgentConstants {

    private AgentConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    public static final int DEFAULT_TIMEOUT_MS = 30000;
    public static final int MAX_CONCURRENT_EXECUTIONS = 10;
    public static final int MAX_CONVERSATION_HISTORY_SIZE = 100;

    public static final String AGENT_TYPE_CONVERSATION = "conversation";
    public static final String AGENT_TYPE_RAG = "rag";
    public static final String AGENT_TYPE_TOOL = "tool";
    public static final String AGENT_TYPE_PLANNING = "planning";
    public static final String AGENT_TYPE_REACT = "react";
    public static final String AGENT_TYPE_MULTIAGENT = "multiagent";
    public static final String AGENT_TYPE_WORKFLOW = "workflow";

}
