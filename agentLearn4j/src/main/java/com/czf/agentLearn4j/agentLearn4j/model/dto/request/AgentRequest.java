package com.czf.agentLearn4j.agentLearn4j.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Base Agent Request DTO
 * Request body for agent execution endpoints
 *
 * @author System
 * @date 2026-01-14
 */
@Data
public class AgentRequest {

    /**
     * Type of agent to execute (e.g., "conversation", "rag", "tool", "planning")
     */
    @NotBlank(message = "agentType cannot be null or empty")
    @Size(max = 50, message = "agentType length cannot exceed 50 characters")
    private String agentType;

    /**
     * Input text for the agent to process
     */
    @NotBlank(message = "input cannot be null or empty")
    @Size(max = 10000, message = "input length cannot exceed 10000 characters")
    private String input;
}
