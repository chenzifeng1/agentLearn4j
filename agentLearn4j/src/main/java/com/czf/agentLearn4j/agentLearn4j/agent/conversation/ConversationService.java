package com.czf.agentLearn4j.agentLearn4j.agent.conversation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Conversation Service
 * Manages conversation flow and message processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {

    private final MessageProcessor messageProcessor;
    private final ConversationMemory conversationMemory;

    /**
     * Process a message and return response
     */
    public String processMessage(String input) {
        // Process the message
        String processedInput = messageProcessor.process(input);

        // Add to memory
        conversationMemory.addMessage("user", processedInput);

        // Generate response (placeholder for now)
        String response = generateResponse(processedInput);

        // Add response to memory
        conversationMemory.addMessage("assistant", response);

        return response;
    }

    private String generateResponse(String input) {
        // Placeholder implementation
        return "Response to: " + input;
    }

}
