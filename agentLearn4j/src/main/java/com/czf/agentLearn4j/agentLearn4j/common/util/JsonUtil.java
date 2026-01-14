package com.czf.agentLearn4j.agentLearn4j.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * JSON Utility Class
 * Provides JSON serialization and deserialization utilities
 *
 * @author System
 * @date 2026-01-14
 */
@Slf4j
public final class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Convert object to JSON string
     *
     * @param obj Object to convert
     * @return JSON string
     * @throws JsonProcessingException if serialization fails
     */
    public static String toJson(Object obj) {
        ValidationUtil.requireNonNull(obj, "Object to serialize cannot be null");

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON: {}", obj.getClass().getName(), e);
            throw new IllegalArgumentException("JSON serialization failed", e);
        }
    }

    /**
     * Convert object to JSON string, returning Optional
     *
     * @param obj Object to convert
     * @return Optional containing JSON string, or empty if serialization fails
     */
    public static Optional<String> toJsonSafe(Object obj) {
        if (obj == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON", e);
            return Optional.empty();
        }
    }

    /**
     * Parse JSON string to object
     *
     * @param json  JSON string to parse
     * @param clazz Target class type
     * @param <T>   Type parameter
     * @return Parsed object
     * @throws IllegalArgumentException if parsing fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        ValidationUtil.requireNonEmpty(json, "JSON string cannot be null or empty");
        ValidationUtil.requireNonNull(clazz, "Target class cannot be null");

        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON to {}: {}", clazz.getName(), json, e);
            throw new IllegalArgumentException("JSON deserialization failed", e);
        }
    }

    /**
     * Parse JSON string to object, returning Optional
     *
     * @param json  JSON string to parse
     * @param clazz Target class type
     * @param <T>   Type parameter
     * @return Optional containing parsed object, or empty if parsing fails
     */
    public static <T> Optional<T> fromJsonSafe(String json, Class<T> clazz) {
        if (ValidationUtil.isNullOrEmpty(json) || clazz == null) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(objectMapper.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to object", e);
            return Optional.empty();
        }
    }

}
