package ru.practicum.explorewithme.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends EwmException {
    public BadRequestException(String message, String reason) {
        super(message, HttpStatus.BAD_REQUEST, reason);
    }
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "Incorrectly made request.");
    }
}
