package com.czf.agentLearn4j.agentLearn4j.controller;

import com.czf.agentLearn4j.agentLearn4j.common.constants.ApiConstants;
import com.czf.agentLearn4j.agentLearn4j.model.dto.request.AgentRequest;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.AgentResponse;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.ApiResponse;
import com.czf.agentLearn4j.agentLearn4j.service.AgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * RAG Controller
 * REST endpoints for RAG agent
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.RAG_PATH)
@RequiredArgsConstructor
public class RagController {

    private final AgentService agentService;

    @PostMapping("/query")
    public ApiResponse<AgentResponse> query(@RequestBody AgentRequest request) {
        log.info("RAG query request: {}", request);
        AgentResponse response = agentService.executeAgent("rag", request.getInput());
        return ApiResponse.success(response);
    }

}
