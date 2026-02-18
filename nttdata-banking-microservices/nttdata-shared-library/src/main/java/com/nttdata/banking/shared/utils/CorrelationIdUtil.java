package com.nttdata.banking.shared.utils;

import org.slf4j.MDC;

import java.util.UUID;

public final class CorrelationIdUtil {

    private static final String CORRELATION_ID_KEY = "correlationId";

    private CorrelationIdUtil() {
        // Utility class
    }

    /**
     * Generate a new correlation ID and store in MDC
     */
    public static String generateCorrelationId() {
        String correlationId = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID_KEY, correlationId);
        return correlationId;
    }

    /**
     * Get current correlation ID from MDC
     */
    public static String getCorrelationId() {
        String correlationId = MDC.get(CORRELATION_ID_KEY);
        if (correlationId == null) {
            correlationId = generateCorrelationId();
        }
        return correlationId;
    }

    /**
     * Clear correlation ID from MDC
     */
    public static void clear() {
        MDC.remove(CORRELATION_ID_KEY);
    }
}