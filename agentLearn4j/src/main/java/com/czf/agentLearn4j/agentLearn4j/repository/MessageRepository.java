package com.czf.agentLearn4j.agentLearn4j.repository;

import com.czf.agentLearn4j.agentLearn4j.model.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Message Repository
 * Data access for messages (placeholder for JPA interface)
 */
@Repository
public interface MessageRepository {
    // When using JPA: extends JpaRepository<Message, String>

    // Placeholder methods
    Message findById(String id);
    List<Message> findByConversationId(String conversationId);
    Message save(Message message);
}
