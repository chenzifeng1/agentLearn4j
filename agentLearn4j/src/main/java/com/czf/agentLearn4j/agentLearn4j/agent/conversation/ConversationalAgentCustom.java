package com.czf.agentLearn4j.agentLearn4j.agent.conversation;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.internal.node.Node;
import com.czf.agentLearn4j.agentLearn4j.agent.core.CustomBaseAgent;
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
public class ConversationalAgentCustom extends CustomBaseAgent {

    private final ConversationService conversationService;

    /**
     * 使用简化构造方法 - 只需要传入name和description
     * 其他参数使用默认值
     */
    public ConversationalAgentCustom(ConversationService conversationService) {
        super("ConversationalAgent", "Conversational AI for question-answer interactions");
        this.conversationService = conversationService;
    }

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_CONVERSATION;
    }

    @Override
    public String execute(String input) {
        return conversationService.processMessage(input);
    }

    @Override
    public Node asNode(boolean includeContents, boolean returnReasoningContents) {
        return null;
    }

    @Override
    protected StateGraph initGraph() throws GraphStateException {
        return null;
    }
}
