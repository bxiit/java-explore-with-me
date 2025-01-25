package ru.practicum.explorewithme.service.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.explorewithme.service.validation.MinDuration;

import java.time.Duration;
import java.time.Instant;

public class ValidDateValidator implements ConstraintValidator<MinDuration, Instant> {

    private Duration minDuration;

    @Override
    public void initialize(MinDuration ann) {
        String durationFromNow = ann.durationFromNow();
        minDuration = Duration.parse(durationFromNow);
    }

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (value == null || value.isAfter(Instant.now().plus(minDuration))) {
            return true;
        }
        return value.isAfter(Instant.now().plus(minDuration));
    }
}
