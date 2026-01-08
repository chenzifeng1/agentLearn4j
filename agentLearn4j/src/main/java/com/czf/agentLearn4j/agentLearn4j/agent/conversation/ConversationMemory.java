package com.czf.agentLearn4j.agentLearn4j.agent.conversation;

import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Conversation Memory
 * Maintains conversation history
 */
@Slf4j
@Component
public class ConversationMemory {

    private final List<MessageEntry> history = new ArrayList<>();

    /**
     * Add message to history
     */
    public void addMessage(String role, String content) {
        MessageEntry entry = new MessageEntry(role, content, System.currentTimeMillis());
        history.add(entry);

        // Limit history size
        if (history.size() > AgentConstants.MAX_CONVERSATION_HISTORY_SIZE) {
            history.remove(0);
        }

        log.debug("Added message to history. Total messages: {}", history.size());
    }

    /**
     * Get conversation history
     */
    public List<MessageEntry> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * Clear history
     */
    public void clear() {
        history.clear();
        log.debug("Cleared conversation history");
    }

    @Data
    public static class MessageEntry {
        private final String role;
        private final String content;
        private final Long timestamp;
    }

}
