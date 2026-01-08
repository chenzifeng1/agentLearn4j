package com.czf.agentLearn4j.agentLearn4j.agent.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tool Registry
 * Registers and manages available tools
 */
@Slf4j
@Component
public class ToolRegistry {

    private final Map<String, Object> tools = new ConcurrentHashMap<>();

    /**
     * Register a tool
     */
    public void registerTool(String toolName, Object tool) {
        tools.put(toolName, tool);
        log.info("Tool registered: {}", toolName);
    }

    /**
     * Get tool by name
     */
    public Object getTool(String toolName) {
        return tools.get(toolName);
    }

    /**
     * Check if tool exists
     */
    public boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }

    /**
     * Get all registered tools
     */
    public Map<String, Object> getAllTools() {
        return tools;
    }

}
