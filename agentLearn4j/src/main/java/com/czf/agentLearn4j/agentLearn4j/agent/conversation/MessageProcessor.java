package com.czf.agentLearn4j.agentLearn4j.agent.conversation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Message Processor
 * Processes and validates messages
 */
@Slf4j
@Component
public class MessageProcessor {

    /**
     * Process message (clean, validate, etc.)
     */
    public String process(String message) {
        if (message == null) {
            return "";
        }

        // Trim whitespace
        String processed = message.trim();

        // Additional processing can be added here
        log.debug("Processed message: {}", processed);

        return processed;
    }

}
