package com.czf.agentLearn4j.agentLearn4j.agent.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Document Retriever
 * Retrieves relevant documents based on query
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentRetriever {

    private final VectorStoreService vectorStoreService;

    /**
     * Retrieve relevant documents for query
     */
    public String retrieve(String query) {
        // Get similar documents from vector store
        return vectorStoreService.search(query);
    }

}
