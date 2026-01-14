package com.czf.agentLearn4j.agentLearn4j.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;

/**
 * Web MVC Configuration
 * Configures web-related settings like CORS, interceptors, etc.
 *
 * Security Notes:
 * - CORS configuration should be restrictive in production
 * - allowedOrigins("*") is a security risk and should specify exact domains
 * - When using credentials, must specify exact origins (not "*")
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS with allowed origins: {}", (Object) allowedOrigins);

        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false) // Set to true if you need to send cookies/auth headers
                .maxAge(3600); // Cache preflight response for 1 hour

        // TODO: In production, consider:
        // 1. Setting allowCredentials(true) with specific origins for authenticated requests
        // 2. Restricting allowedHeaders to specific headers instead of "*"
        // 3. Adding exposedHeaders if needed by frontend
        // 4. Consider using Spring Security's CORS configuration for better integration
    }

}
