package com.czf.agentLearn4j.agentLearn4j.agent.tool.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Web Search Tool
 * Example tool for web search
 */
@Slf4j
@Component
public class WebSearchTool {

    /**
     * Search the web
     */
    public String search(String query) {
        log.debug("Searching web for: {}", query);
        // Placeholder implementation
        // In real implementation, call actual web search API
        return "Search results for: " + query;
    }

}
