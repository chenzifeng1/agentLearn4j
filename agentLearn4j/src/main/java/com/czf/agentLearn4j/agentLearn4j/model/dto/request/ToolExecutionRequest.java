package com.czf.agentLearn4j.agentLearn4j.model.dto.request;

import lombok.Data;
import java.util.Map;

/**
 * Tool Execution Request DTO
 */
@Data
public class ToolExecutionRequest {
    private String toolName;
    private Map<String, Object> parameters;
    private String input;
}
