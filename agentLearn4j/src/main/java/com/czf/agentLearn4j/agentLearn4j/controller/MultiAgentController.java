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
 * Multi-Agent Controller
 * REST endpoints for multi-agent coordination
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.MULTIAGENT_PATH)
@RequiredArgsConstructor
public class MultiAgentController {

    private final AgentService agentService;

    @PostMapping("/collaborate")
    public ApiResponse<AgentResponse> collaborate(@RequestBody AgentRequest request) {
        log.info("Multi-agent collaboration request: {}", request);
        AgentResponse response = agentService.executeAgent("multiagent", request.getInput());
        return ApiResponse.success(response);
    }

}
