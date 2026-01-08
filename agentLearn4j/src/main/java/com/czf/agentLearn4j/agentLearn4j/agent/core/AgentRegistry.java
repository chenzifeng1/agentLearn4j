package com.czf.agentLearn4j.agentLearn4j.agent.core;

import com.czf.agentLearn4j.agentLearn4j.common.exception.AgentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agent Registry
 * Service locator for agent discovery and registration
 */
@Slf4j
@Component
public class AgentRegistry {

    private final Map<String, BaseAgent> agents = new ConcurrentHashMap<>();

    /**
     * Register an agent
     */
    public void registerAgent(String agentType, BaseAgent agent) {
        agents.put(agentType, agent);
        log.info("Agent registered: {}", agentType);
    }

    /**
     * Get agent by type
     */
    public BaseAgent getAgent(String agentType) {
        BaseAgent agent = agents.get(agentType);
        if (agent == null) {
            throw new AgentNotFoundException(agentType);
        }
        return agent;
    }

    /**
     * Check if agent exists
     */
    public boolean hasAgent(String agentType) {
        return agents.containsKey(agentType);
    }

    /**
     * Get all registered agents
     */
    public Map<String, BaseAgent> getAllAgents() {
        return agents;
    }

}
