package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}
