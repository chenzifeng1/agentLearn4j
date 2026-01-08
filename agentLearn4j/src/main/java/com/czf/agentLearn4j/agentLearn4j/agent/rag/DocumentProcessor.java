package com.czf.agentLearn4j.agentLearn4j.agent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Document Processor
 * Preprocesses documents for indexing
 */
@Slf4j
@Component
public class DocumentProcessor {

    private static final int DEFAULT_CHUNK_SIZE = 512;
    private static final int DEFAULT_CHUNK_OVERLAP = 50;

    /**
     * Split document into chunks
     */
    public List<String> chunkDocument(String document) {
        return chunkDocument(document, DEFAULT_CHUNK_SIZE, DEFAULT_CHUNK_OVERLAP);
    }

    /**
     * Split document into chunks with custom size and overlap
     */
    public List<String> chunkDocument(String document, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();

        if (document == null || document.isEmpty()) {
            return chunks;
        }

        int start = 0;
        while (start < document.length()) {
            int end = Math.min(start + chunkSize, document.length());
            chunks.add(document.substring(start, end));
            start += (chunkSize - overlap);
        }

        log.debug("Split document into {} chunks", chunks.size());
        return chunks;
    }

}
