package com.czf.agentLearn4j.agentLearn4j.agent.multiagent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Agent Communicator
 * Handles inter-agent communication
 */
@Slf4j
@Component
public class AgentCommunicator {

    /**
     * Send message between agents
     */
    public void sendMessage(String fromAgent, String toAgent, String message) {
        log.debug("Message from {} to {}: {}", fromAgent, toAgent, message);
    }

}
