package ru.practicum.explorewithme.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends EwmException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "The required object was not found.");
    }

    public NotFoundException(Class<?> aClass, Object id) {
        super(aClass.getSimpleName() + " with id=" + id + " was not found", HttpStatus.NOT_FOUND, "The required object was not found.");
    }
}
