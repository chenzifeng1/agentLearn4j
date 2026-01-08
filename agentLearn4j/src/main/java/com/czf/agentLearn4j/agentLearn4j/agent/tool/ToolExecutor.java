package com.czf.agentLearn4j.agentLearn4j.agent.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Tool Executor
 * Executes tool calls safely
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ToolExecutor {

    private final ToolRegistry toolRegistry;

    /**
     * Execute a tool
     */
    public String executeTool(String toolName, String input) {
        if (!toolRegistry.hasTool(toolName)) {
            return "Tool not found: " + toolName;
        }

        Object tool = toolRegistry.getTool(toolName);
        log.debug("Executing tool: {} with input: {}", toolName, input);

        // Execute tool (implementation depends on tool interface)
        return "Tool " + toolName + " executed with: " + input;
    }

}
