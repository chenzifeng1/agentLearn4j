package com.czf.agentLearn4j.agentLearn4j.agent.multiagent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Result Aggregator
 * Combines results from multiple agents
 */
@Slf4j
@Component
public class ResultAggregator {

    /**
     * Aggregate results
     */
    public String aggregate(String results) {
        log.debug("Aggregating results: {}", results);
        return "Aggregated results: " + results;
    }

}
