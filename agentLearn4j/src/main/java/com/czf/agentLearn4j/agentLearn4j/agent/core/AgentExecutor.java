package com.czf.agentLearn4j.agentLearn4j.agent.core;

import com.czf.agentLearn4j.agentLearn4j.common.exception.AgentExecutionException;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.AgentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Agent Executor
 * Orchestrates agent execution with error handling and metrics
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgentExecutor {

    private final AgentRegistry agentRegistry;

    /**
     * Execute agent with timing and error handling
     */
    public AgentResponse executeAgent(String agentType, String input) {
        long startTime = System.currentTimeMillis();

        try {
            CustomBaseAgent agent = agentRegistry.getAgent(agentType);

            if (!agent.validateInput(input)) {
                throw new AgentExecutionException("Invalid input for agent: " + agentType);
            }

            agent.beforeExecute(input);
            String output = agent.execute(input);
            agent.afterExecute(output);

            long executionTime = System.currentTimeMillis() - startTime;

            return AgentResponse.builder()
                    .agentType(agentType)
                    .output(output)
                    .status("SUCCESS")
                    .executionTimeMs(executionTime)
                    .build();

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Agent execution failed: {}", e.getMessage(), e);

            return AgentResponse.builder()
                    .agentType(agentType)
                    .output(e.getMessage())
                    .status("FAILED")
                    .executionTimeMs(executionTime)
                    .build();
        }
    }

}
