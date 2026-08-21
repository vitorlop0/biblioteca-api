package com.vitor.library_api.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(String message, int status, LocalDateTime timestamp) {
}
