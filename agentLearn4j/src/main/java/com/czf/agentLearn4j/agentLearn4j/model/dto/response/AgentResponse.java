package com.czf.agentLearn4j.agentLearn4j.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {
    private String agentType;
    private String output;
    private String status;
    private Long executionTimeMs;
}
