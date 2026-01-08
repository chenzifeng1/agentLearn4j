package com.czf.agentLearn4j.agentLearn4j.service;

import com.czf.agentLearn4j.agentLearn4j.agent.rag.DocumentProcessor;
import com.czf.agentLearn4j.agentLearn4j.agent.rag.VectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Document Service
 * Manages document operations for RAG
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentProcessor documentProcessor;
    private final VectorStoreService vectorStoreService;

    /**
     * Add document to vector store
     */
    public void addDocument(String content) {
        log.info("Adding document to vector store");

        // Split into chunks
        List<String> chunks = documentProcessor.chunkDocument(content);

        // Add each chunk to vector store
        for (String chunk : chunks) {
            vectorStoreService.addDocument(chunk);
        }

        log.info("Document added successfully. Total chunks: {}", chunks.size());
    }

}
