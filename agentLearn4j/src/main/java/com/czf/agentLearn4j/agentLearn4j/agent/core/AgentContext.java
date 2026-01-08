package com.czf.agentLearn4j.agentLearn4j.agent.core;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * Agent Context
 * Thread-safe context holder for agent execution state
 */
@Data
public class AgentContext {

    private static final ThreadLocal<AgentContext> contextHolder = new ThreadLocal<>();

    private String executionId;
    private String userId;
    private Map<String, Object> metadata;

    public AgentContext() {
        this.metadata = new HashMap<>();
    }

    /**
     * Get current context
     */
    public static AgentContext getCurrentContext() {
        AgentContext context = contextHolder.get();
        if (context == null) {
            context = new AgentContext();
            contextHolder.set(context);
        }
        return context;
    }

    /**
     * Clear current context
     */
    public static void clearContext() {
        contextHolder.remove();
    }

    /**
     * Set metadata
     */
    public void setMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    /**
     * Get metadata
     */
    public Object getMetadata(String key) {
        return this.metadata.get(key);
    }

}
