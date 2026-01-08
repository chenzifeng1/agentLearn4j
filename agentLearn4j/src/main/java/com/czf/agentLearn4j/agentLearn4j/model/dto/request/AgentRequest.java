package com.czf.agentLearn4j.agentLearn4j.model.dto.request;

import lombok.Data;

/**
 * Base Agent Request DTO
 */
@Data
public class AgentRequest {
    private String agentType;
    private String input;
}
