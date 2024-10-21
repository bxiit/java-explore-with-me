package ru.practicum.explorewithme.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public abstract class EwmException extends RuntimeException {
    protected final String status;
    protected final String reason;
    protected final Instant timestamp;

    public EwmException(String message, HttpStatus status, String reason) {
        super(message);
        this.status = status.name();
        this.reason = reason;
        this.timestamp = Instant.now();
    }
}
