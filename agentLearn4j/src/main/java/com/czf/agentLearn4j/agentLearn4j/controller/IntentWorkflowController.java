package com.czf.agentLearn4j.agentLearn4j.controller;

import com.czf.agentLearn4j.agentLearn4j.agent.workflow.IntentBasedWorkflowAgent;
import com.czf.agentLearn4j.agentLearn4j.model.dto.request.AgentRequest;
import com.czf.agentLearn4j.agentLearn4j.model.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Intent Workflow Controller
 * 提供意图工作流的 REST 接口，支持流式响应
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/intent-workflow")
@RequiredArgsConstructor
public class IntentWorkflowController {

    private final IntentBasedWorkflowAgent workflowAgent;

    /**
     * 同步执行工作流
     *
     * @param request 包含用户输入的请求
     * @return 工作流执行结果
     */
    @PostMapping("/execute")
    public ApiResponse<String> execute(@RequestBody AgentRequest request) {
        log.info("收到同步工作流请求: {}", request);

        try {
            String result = workflowAgent.execute(request.getInput());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("工作流执行失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 流式执行工作流 - 使用 Server-Sent Events (SSE)
     *
     * 客户端可以实时接收工作流的执行进度和结果
     *
     * @param request 包含用户输入的请求
     * @return SSE 流
     *
     * 示例事件流：
     * data: {"event":"start","message":"工作流开始执行"}
     * data: {"event":"intent_recognition","message":"正在识别意图..."}
     * data: {"event":"intent_recognized","intent":"QUERY"}
     * data: {"event":"routing","message":"正在路由请求..."}
     * data: {"event":"routed","route":"knowledge_query"}
     * data: {"event":"processing","message":"正在处理请求..."}
     * data: {"event":"result","output":"这是查询结果"}
     * data: {"event":"complete","message":"工作流执行完成"}
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody AgentRequest request) {
        log.info("收到流式工作流请求: {}", request);

        return workflowAgent.streamExecute(request.getInput())
                .doOnError(e -> log.error("流式工作流执行失败", e))
                .doOnComplete(() -> log.info("流式工作流执行完成"));
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("Intent Workflow Agent is healthy");
    }
}
