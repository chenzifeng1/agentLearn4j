package com.czf.agentLearn4j.agentLearn4j.repository;

import com.czf.agentLearn4j.agentLearn4j.model.entity.AgentExecution;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Agent Execution Repository
 * Data access for agent executions (placeholder for JPA interface)
 */
@Repository
public interface AgentExecutionRepository {
    // When using JPA: extends JpaRepository<AgentExecution, String>

    // Placeholder methods
    AgentExecution findById(String id);
    List<AgentExecution> findByAgentType(String agentType);
    AgentExecution save(AgentExecution execution);
}
