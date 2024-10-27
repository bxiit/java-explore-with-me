package ru.practicum.explorewithme.service.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.explorewithme.service.validation.NotZeroLongId;

import java.util.List;

public class NotZeroIdValidator implements ConstraintValidator<NotZeroLongId, List<Long>> {
    @Override
    public boolean isValid(List<Long> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.contains(0L);
    }
}
