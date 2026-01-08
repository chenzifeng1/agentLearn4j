package com.czf.agentLearn4j.agentLearn4j.controller;

import com.czf.agentLearn4j.agentLearn4j.common.constants.ApiConstants;
import com.czf.agentLearn4j.agentLearn4j.model.dto.request.ToolExecutionRequest;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.AgentResponse;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.ApiResponse;
import com.czf.agentLearn4j.agentLearn4j.service.AgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Tool Controller
 * REST endpoints for tool agent
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.TOOLS_PATH)
@RequiredArgsConstructor
public class ToolController {

    private final AgentService agentService;

    @PostMapping("/execute")
    public ApiResponse<AgentResponse> execute(@RequestBody ToolExecutionRequest request) {
        log.info("Tool execution request: {}", request);
        AgentResponse response = agentService.executeAgent("tool", request.getInput());
        return ApiResponse.success(response);
    }

}
