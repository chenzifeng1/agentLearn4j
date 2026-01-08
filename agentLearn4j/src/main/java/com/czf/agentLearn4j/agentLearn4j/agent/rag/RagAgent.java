package com.czf.agentLearn4j.agentLearn4j.agent.rag;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RAG (Retrieval Augmented Generation) Agent
 * Combines document retrieval with AI generation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RagAgent extends BaseAgent {

    private final DocumentRetriever documentRetriever;

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_RAG;
    }

    @Override
    public String execute(String input) {
        // Retrieve relevant documents
        String retrievedContext = documentRetriever.retrieve(input);

        // Generate response with context (placeholder)
        return generateWithContext(input, retrievedContext);
    }

    private String generateWithContext(String query, String context) {
        // Placeholder implementation
        return "RAG Response for: " + query + " with context: " + context;
    }

}
