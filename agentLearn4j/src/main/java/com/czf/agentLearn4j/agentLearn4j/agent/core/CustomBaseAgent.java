package com.czf.agentLearn4j.agentLearn4j.agent.core;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.agent.BaseAgent;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.internal.node.Node;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract Base Agent
 * Foundation class for all agent implementations
 */
@Slf4j
public abstract class CustomBaseAgent extends BaseAgent {

    /**
     * Full constructor with all parameters
     */
    public CustomBaseAgent(String name, String description, boolean includeContents,
                          boolean returnReasoningContents, String outputKey, KeyStrategy outputKeyStrategy) {
        super(name, description, includeContents, returnReasoningContents, outputKey, outputKeyStrategy);
    }

    /**
     * Simplified constructor with commonly used parameters
     * Uses default values: includeContents=true, returnReasoningContents=false
     */
    public CustomBaseAgent(String name, String description) {
        this(name, description, true, false, "output", KeyStrategy.APPEND);
    }

    /**
     * Most simplified constructor - only requires name
     * Uses description from agent type and default values
     */
    public CustomBaseAgent(String name) {
        this(name, name + " Agent", true, false, "output", KeyStrategy.APPEND);
    }

    /**
     * Get agent type identifier
     */
    public abstract String getAgentType();

    /**
     * Execute agent logic
     * @param input User input
     * @return Agent output
     */
    public abstract String execute(String input);

    /**
     * Validate input before execution
     * @param input User input
     * @return true if valid
     */
    protected boolean validateInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Pre-execution hook
     */
    protected void beforeExecute(String input) {
        log.debug("Agent {} executing with input: {}", getAgentType(), input);
    }

    /**
     * Post-execution hook
     */
    protected void afterExecute(String output) {
        log.debug("Agent {} completed with output: {}", getAgentType(), output);
    }

    /**
     * Convert agent to a node in the workflow graph
     * @param includeContents whether to include contents
     * @param returnReasoningContents whether to return reasoning contents
     * @return Node representation of this agent
     */
    @Override
    public Node asNode(boolean includeContents, boolean returnReasoningContents) {
        return null;
    }

    /**
     * Initialize the state graph for this agent
     * Override this method in subclasses if you need to define a custom workflow graph
     * @return StateGraph for the agent workflow
     * @throws GraphStateException if graph initialization fails
     */
    @Override
    protected StateGraph initGraph() throws GraphStateException {
        return null;
    }

}
