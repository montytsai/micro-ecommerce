package io.github.montytsai.ecommerce.handler;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        String path,
        String errorMessage,
        Map<String, String> errorDetails
) {
    public ErrorResponse(String path, String message, Map<String, String> errors) {
        this(
                LocalDateTime.now(),
                path,
                message,
                errors != null ? errors : Map.of()
        );
    }

    public ErrorResponse(String path, String message) {
        this(path, message, Map.of());
    }
}