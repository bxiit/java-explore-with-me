package ru.practicum.explorewithme.service.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends EwmException{
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT, "For the requested operation the conditions are not met.");
    }
    public ConflictException(String message, String reason) {
        super(message, HttpStatus.CONFLICT, reason);
    }
}
