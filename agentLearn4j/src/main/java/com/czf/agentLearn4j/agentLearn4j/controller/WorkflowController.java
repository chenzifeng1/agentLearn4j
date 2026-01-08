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
 * Workflow Controller
 * REST endpoints for workflow agent
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.WORKFLOW_PATH)
@RequiredArgsConstructor
public class WorkflowController {

    private final AgentService agentService;

    @PostMapping("/execute")
    public ApiResponse<AgentResponse> execute(@RequestBody AgentRequest request) {
        log.info("Workflow execution request: {}", request);
        AgentResponse response = agentService.executeAgent("workflow", request.getInput());
        return ApiResponse.success(response);
    }

}
