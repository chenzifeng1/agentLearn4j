package com.czf.agentLearn4j.agentLearn4j.common.constants;

/**
 * API Constants
 */
public final class ApiConstants {

    private ApiConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;

    public static final String CONVERSATION_PATH = API_BASE_PATH + "/conversation";
    public static final String RAG_PATH = API_BASE_PATH + "/rag";
    public static final String TOOLS_PATH = API_BASE_PATH + "/tools";
    public static final String PLANNING_PATH = API_BASE_PATH + "/planning";
    public static final String REACT_PATH = API_BASE_PATH + "/react";
    public static final String MULTIAGENT_PATH = API_BASE_PATH + "/multiagent";
    public static final String WORKFLOW_PATH = API_BASE_PATH + "/workflow";

}
