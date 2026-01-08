package com.czf.agentLearn4j.agentLearn4j.model.dto.request;

import lombok.Data;

/**
 * Conversation Request DTO
 */
@Data
public class ConversationRequest {
    private String conversationId;
    private String message;
    private String userId;
}
