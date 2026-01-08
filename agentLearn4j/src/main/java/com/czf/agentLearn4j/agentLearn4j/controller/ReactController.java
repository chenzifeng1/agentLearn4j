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
 * ReAct Controller
 * REST endpoints for ReAct agent
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.REACT_PATH)
@RequiredArgsConstructor
public class ReactController {

    private final AgentService agentService;

    @PostMapping("/solve")
    public ApiResponse<AgentResponse> solve(@RequestBody AgentRequest request) {
        log.info("ReAct solve request: {}", request);
        AgentResponse response = agentService.executeAgent("react", request.getInput());
        return ApiResponse.success(response);
    }

}
