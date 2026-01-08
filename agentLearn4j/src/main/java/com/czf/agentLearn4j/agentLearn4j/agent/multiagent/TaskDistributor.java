package com.czf.agentLearn4j.agentLearn4j.agent.multiagent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Task Distributor
 * Distributes work among multiple agents
 */
@Slf4j
@Component
public class TaskDistributor {

    /**
     * Distribute task to agents
     */
    public String distribute(String task) {
        log.debug("Distributing task: {}", task);
        return "Task distributed: " + task;
    }

}
