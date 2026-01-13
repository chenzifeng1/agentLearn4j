package com.czf.agentLearn4j.agentLearn4j.config;

import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

/**
 * DashScope ChatModel Configuration
 * Configures Alibaba DashScope AI service integration
 *
 * Note: DashScopeChatModel is auto-configured by Spring AI Alibaba starter
 * based on application.properties settings
 */
@Slf4j
@Configuration
public class DashScopeConfig {

    public DashScopeConfig() {
        log.info("DashScope configuration initialized");
    }
}
