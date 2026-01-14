package com.czf.agentLearn4j.agentLearn4j.controller;

import com.czf.agentLearn4j.agentLearn4j.common.constants.ApiConstants;
import com.czf.agentLearn4j.agentLearn4j.model.dto.request.ConversationRequest;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.ApiResponse;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.ConversationResponse;
import com.czf.agentLearn4j.agentLearn4j.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Conversation Controller
 * REST endpoints for conversational agent
 *
 * @author System
 * @date 2026-01-14
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.CONVERSATION_PATH)
@RequiredArgsConstructor
public class ConversationController {

    private final AgentService agentService;

    /**
     * Process a conversational chat request
     *
     * @param request The conversation request containing user message
     * @return API response with conversation result
     */
    @PostMapping("/chat")
    public ApiResponse<ConversationResponse> chat(@Valid @RequestBody ConversationRequest request) {
        log.info("Chat request received: {}", request);
        ConversationResponse response = ConversationResponse.builder()
                .conversationId(request.getConversationId())
                .message(request.getMessage())
                .response("Response from agent")
                .timestamp(System.currentTimeMillis())
                .build();

        return ApiResponse.success(response);
    }

}
