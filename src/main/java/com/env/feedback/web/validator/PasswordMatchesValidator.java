package com.env.feedback.web.validator;

import com.env.feedback.security.password.PasswordMatches;
import com.env.feedback.web.dto.ChangePasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ChangePasswordDto> {

    @Override
    public boolean isValid(ChangePasswordDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.getPassword() != null && value.getPassword().equals(value.getConfirmPassword());
    }
}
