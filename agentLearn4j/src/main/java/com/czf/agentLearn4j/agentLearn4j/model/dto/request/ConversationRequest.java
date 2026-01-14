package com.czf.agentLearn4j.agentLearn4j.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Conversation Request DTO
 * Request body for conversational agent endpoints
 *
 * @author System
 * @date 2026-01-14
 */
@Data
public class ConversationRequest {

    /**
     * Conversation ID (optional for new conversations)
     */
    @Size(max = 100, message = "conversationId length cannot exceed 100 characters")
    private String conversationId;

    /**
     * User message content
     */
    @NotBlank(message = "message cannot be null or empty")
    @Size(max = 5000, message = "message length cannot exceed 5000 characters")
    private String message;

    /**
     * User ID
     */
    @NotBlank(message = "userId cannot be null or empty")
    @Size(max = 100, message = "userId length cannot exceed 100 characters")
    private String userId;
}
