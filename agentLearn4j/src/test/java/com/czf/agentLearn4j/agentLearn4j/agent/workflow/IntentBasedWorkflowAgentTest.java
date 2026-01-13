package com.czf.agentLearn4j.agentLearn4j.agent.workflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntentBasedWorkflowAgent 测试类
 */
@Slf4j
@SpringBootTest
class IntentBasedWorkflowAgentTest {

    @Autowired(required = false)
    private IntentBasedWorkflowAgent workflowAgent;

    @Test
    void testQueryIntent() {
        if (workflowAgent == null) {
            log.warn("IntentBasedWorkflowAgent not available, skipping test");
            return;
        }

        String input = "什么是 Spring AI？";
        String result = workflowAgent.execute(input);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        log.info("查询意图测试结果: {}", result);
    }

    @Test
    void testChatIntent() {
        if (workflowAgent == null) {
            log.warn("IntentBasedWorkflowAgent not available, skipping test");
            return;
        }

        String input = "你好，今天天气怎么样？";
        String result = workflowAgent.execute(input);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        log.info("对话意图测试结果: {}", result);
    }

    @Test
    void testUnknownIntent() {
        if (workflowAgent == null) {
            log.warn("IntentBasedWorkflowAgent not available, skipping test");
            return;
        }

        String input = "测试未知意图";
        String result = workflowAgent.execute(input);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        log.info("未知意图测试结果: {}", result);
    }

    @Test
    void testStreamExecute() {
        if (workflowAgent == null) {
            log.warn("IntentBasedWorkflowAgent not available, skipping test");
            return;
        }

        String input = "Spring Boot 的优势有哪些？";

        workflowAgent.streamExecute(input)
                .doOnNext(event -> log.info("收到事件: {}", event))
                .blockLast();

        log.info("流式执行测试完成");
    }
}
