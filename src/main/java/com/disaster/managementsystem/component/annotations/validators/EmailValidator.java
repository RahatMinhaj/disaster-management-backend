package com.disaster.managementsystem.component.annotations.validators;

import com.disaster.managementsystem.component.annotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email.isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email field is null!").addConstraintViolation().disableDefaultConstraintViolation();
            return false;
        }
        String EMAIL_PATTERN = "^([A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
