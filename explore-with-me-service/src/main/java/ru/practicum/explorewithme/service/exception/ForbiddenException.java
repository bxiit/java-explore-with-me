package ru.practicum.explorewithme.service.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends EwmException {
    public ForbiddenException(String message, HttpStatus status, String reason) {
        super(message, status, reason);
    }
}
