package com.czf.agentLearn4j.agentLearn4j.service;

import com.czf.agentLearn4j.agentLearn4j.agent.core.AgentExecutor;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.AgentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Agent Service
 * Core service for agent operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentExecutor agentExecutor;

    /**
     * Execute agent by type
     */
    public AgentResponse executeAgent(String agentType, String input) {
        log.info("Executing agent: {} with input: {}", agentType, input);
        return agentExecutor.executeAgent(agentType, input);
    }

}
