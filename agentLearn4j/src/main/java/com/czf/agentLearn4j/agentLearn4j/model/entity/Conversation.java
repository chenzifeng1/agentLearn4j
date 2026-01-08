package com.czf.agentLearn4j.agentLearn4j.model.entity;

import lombok.Data;

/**
 * Conversation Entity
 * Domain model for conversation (can be extended with JPA annotations if using database)
 */
@Data
public class Conversation {
    private String id;
    private String userId;
    private Long createdAt;
    private Long updatedAt;
}
