package com.czf.agentLearn4j.agentLearn4j.agent.tool.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Calculator Tool
 * Example tool for mathematical calculations
 */
@Slf4j
@Component
public class CalculatorTool {

    /**
     * Calculate expression
     */
    public double calculate(String expression) {
        log.debug("Calculating: {}", expression);
        // Placeholder implementation
        // In real implementation, parse and evaluate mathematical expressions
        return 42.0;
    }

    /**
     * Add two numbers
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtract two numbers
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiply two numbers
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divide two numbers
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

}
