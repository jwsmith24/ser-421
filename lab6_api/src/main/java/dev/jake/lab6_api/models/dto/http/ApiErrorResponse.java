package dev.jake.lab6_api.models.dto.http;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String message,
        int status,
        LocalDateTime timeStamp
) {
}
