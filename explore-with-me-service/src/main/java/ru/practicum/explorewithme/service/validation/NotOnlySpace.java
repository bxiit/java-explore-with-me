package ru.practicum.explorewithme.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotOnlySpaceStringValidator.class)
public @interface NotOnlySpace {
    String message() default "Field has only spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
