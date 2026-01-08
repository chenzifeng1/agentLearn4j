package com.czf.agentLearn4j.agentLearn4j.agent.planning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Plan Validator
 * Validates plan feasibility
 */
@Slf4j
@Component
public class PlanValidator {

    /**
     * Validate plan
     */
    public boolean validate(String plan) {
        if (plan == null || plan.isEmpty()) {
            log.warn("Plan is null or empty");
            return false;
        }

        log.debug("Validating plan: {}", plan);
        return true;
    }

}
