package com.example.taskboard.web.error;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String error,
        String message,
        int status,
        String path,
        Instant timestamp,
        Map<String, String> validationErrors
) {
    public static ApiError of(
            String error,
            String message,
            int status,
            String path
    ) {
        return new ApiError(error, message, status, path, Instant.now(), null);
    }

    public static ApiError withValidationErrors(
            String error,
            String message,
            int status,
            String path,
            Map<String, String> validationErrors
    ) {
        return new ApiError(error, message, status, path, Instant.now(), validationErrors);
    }
}
