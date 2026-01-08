package com.czf.agentLearn4j.agentLearn4j.agent.tool.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Weather Tool
 * Example tool for querying weather information
 */
@Slf4j
@Component
public class WeatherTool {

    /**
     * Get weather for location
     */
    public String getWeather(String location) {
        log.debug("Getting weather for: {}", location);
        // Placeholder implementation
        return "Weather in " + location + ": Sunny, 25°C";
    }

}
