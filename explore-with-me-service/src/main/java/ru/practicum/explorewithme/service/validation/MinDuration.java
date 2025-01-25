package ru.practicum.explorewithme.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.practicum.explorewithme.service.validation.validator.ValidDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateValidator.class)
public @interface MinDuration {
    String message() default "Время проведения события невалидна";
    String durationFromNow();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
