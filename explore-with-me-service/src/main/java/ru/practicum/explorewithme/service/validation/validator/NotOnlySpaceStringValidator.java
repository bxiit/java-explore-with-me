package ru.practicum.explorewithme.service.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;

public class NotOnlySpaceStringValidator implements ConstraintValidator<NotOnlySpace, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isBlank();
    }
}
