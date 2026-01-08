package com.czf.agentLearn4j.agentLearn4j.service;

import com.czf.agentLearn4j.agentLearn4j.agent.tool.ToolRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Tool Management Service
 * Manages tool registration and operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ToolManagementService {

    private final ToolRegistry toolRegistry;

    /**
     * Get all available tools
     */
    public Map<String, Object> getAvailableTools() {
        log.info("Getting available tools");
        return toolRegistry.getAllTools();
    }

}
