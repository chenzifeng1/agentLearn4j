package com.czf.agentLearn4j.agentLearn4j.repository;

import com.czf.agentLearn4j.agentLearn4j.model.entity.Conversation;
import org.springframework.stereotype.Repository;

/**
 * Conversation Repository
 * Data access for conversations (placeholder for JPA interface)
 */
@Repository
public interface ConversationRepository {
    // When using JPA: extends JpaRepository<Conversation, String>

    // Placeholder methods
    Conversation findById(String id);
    Conversation save(Conversation conversation);
}
