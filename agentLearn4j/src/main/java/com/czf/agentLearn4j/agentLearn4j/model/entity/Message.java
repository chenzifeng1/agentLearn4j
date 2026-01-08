package com.czf.agentLearn4j.agentLearn4j.model.entity;

import lombok.Data;

/**
 * Message Entity
 * Domain model for message (can be extended with JPA annotations if using database)
 */
@Data
public class Message {
    private String id;
    private String conversationId;
    private String role;
    private String content;
    private Long timestamp;
}
