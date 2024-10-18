package ru.practicum.explorewithme.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class NotFoundException extends RuntimeException {

    private static final String status = HttpStatus.NOT_FOUND.name();
    private static final String reason = "The required object was not found.";
    private final Instant timestamp = Instant.now();

    public NotFoundException(String message) {
        super(message);
    }
}
