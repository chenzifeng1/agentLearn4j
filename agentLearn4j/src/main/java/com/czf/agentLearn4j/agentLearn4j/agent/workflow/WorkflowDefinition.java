package com.czf.agentLearn4j.agentLearn4j.agent.workflow;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Workflow Definition
 * Model for workflow configuration
 */
@Data
public class WorkflowDefinition {
    private String name;
    private String description;
    private List<WorkflowStep> steps = new ArrayList<>();

    @Data
    public static class WorkflowStep {
        private String name;
        private String type;
        private String action;
    }

}
