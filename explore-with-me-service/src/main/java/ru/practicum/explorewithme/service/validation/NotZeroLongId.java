package ru.practicum.explorewithme.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.practicum.explorewithme.service.validation.validator.NotZeroIdValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotZeroIdValidator.class)
public @interface NotZeroLongId {
    String message() default "Id can not be 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
