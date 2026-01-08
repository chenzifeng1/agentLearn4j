package com.czf.agentLearn4j.agentLearn4j.model.entity;

import lombok.Data;

/**
 * Agent Execution Entity
 * Domain model for tracking agent executions (can be extended with JPA annotations if using database)
 */
@Data
public class AgentExecution {
    private String id;
    private String agentType;
    private String input;
    private String output;
    private String status;
    private Long executionTimeMs;
    private Long timestamp;
}
