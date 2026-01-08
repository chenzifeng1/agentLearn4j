package com.czf.agentLearn4j.agentLearn4j.common.util;

/**
 * Validation Utility Class
 */
public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonEmpty(String str, String message) {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

}
