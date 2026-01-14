package com.czf.agentLearn4j.agentLearn4j.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Map;

/**
 * Tool Execution Request DTO
 * Request body for tool execution endpoints
 *
 * @author System
 * @date 2026-01-14
 */
@Data
public class ToolExecutionRequest {

    /**
     * Name of the tool to execute
     */
    @NotBlank(message = "toolName cannot be null or empty")
    @Size(max = 50, message = "toolName length cannot exceed 50 characters")
    private String toolName;

    /**
     * Tool parameters (optional)
     */
    private Map<String, Object> parameters;

    /**
     * Input text for the tool
     */
    @NotBlank(message = "input cannot be null or empty")
    @Size(max = 10000, message = "input length cannot exceed 10000 characters")
    private String input;
}
