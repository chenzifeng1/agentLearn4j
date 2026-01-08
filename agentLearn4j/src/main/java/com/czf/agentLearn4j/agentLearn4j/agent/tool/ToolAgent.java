package com.czf.agentLearn4j.agentLearn4j.agent.tool;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Tool Agent
 * Agent that can use external tools/functions
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ToolAgent extends BaseAgent {

    private final ToolExecutor toolExecutor;

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_TOOL;
    }

    @Override
    public String execute(String input) {
        // Parse input to determine which tool to use
        // For now, simple placeholder implementation
        return toolExecutor.executeTool("default", input);
    }

}
