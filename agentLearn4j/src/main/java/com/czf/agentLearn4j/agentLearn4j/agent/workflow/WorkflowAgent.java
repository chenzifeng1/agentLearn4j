package com.czf.agentLearn4j.agentLearn4j.agent.workflow;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Workflow Agent
 * Executes defined workflows
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowAgent extends BaseAgent {

    private final WorkflowEngine workflowEngine;

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_WORKFLOW;
    }

    @Override
    public String execute(String input) {
        // Load workflow definition
        WorkflowDefinition definition = new WorkflowDefinition();
        definition.setName("default-workflow");

        // Execute workflow
        return workflowEngine.execute(definition, input);
    }

}
