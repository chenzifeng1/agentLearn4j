package com.czf.agentLearn4j.agentLearn4j.agent.conversation;

import com.czf.agentLearn4j.agentLearn4j.agent.core.BaseAgent;
import com.czf.agentLearn4j.agentLearn4j.common.constants.AgentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Conversational Agent
 * Basic conversational AI for question-answer interactions
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConversationalAgent extends BaseAgent {

    private final ConversationService conversationService;

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_CONVERSATION;
    }

    @Override
    public String execute(String input) {
        return conversationService.processMessage(input);
    }

}
