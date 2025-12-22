package com.env.feedback.security.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface StrongPassword {
    String message() default "Password does not meet security requirements";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
