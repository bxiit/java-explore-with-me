package ru.practicum.explorewithme.service.controller.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.service.dto.ApiError;
import ru.practicum.explorewithme.service.exception.NotFoundException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final List<String> EMPTY_ERRORS = Collections.emptyList();

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleValidation(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return new ApiError(errors,
                "Incorrectly made request.",
                String.join(", ", errors),
                HttpStatus.BAD_REQUEST.name(),
                Instant.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return new ApiError(errors,
                String.join(", ", errors),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.name(),
                Instant.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFound(NotFoundException e) {
        return new ApiError(EMPTY_ERRORS,
                e.getMessage(), e.getReason(), e.getStatus(), Instant.now());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNumberFormat(NumberFormatException e) {
        return new ApiError(EMPTY_ERRORS,
                "Incorrectly made request.", e.getMessage(), HttpStatus.BAD_REQUEST.name(), Instant.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleConstraint(DataIntegrityViolationException e) {
        return new ApiError(EMPTY_ERRORS,
                "Integrity constraint has been violated.", e.getMessage(), HttpStatus.BAD_REQUEST.name(),
                Instant.now()
        );
    }
}
