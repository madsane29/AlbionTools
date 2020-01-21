package com.albiontools.security.account.validator;

import java.lang.annotation.*;

import javax.validation.*;


@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches { 
    String message() default "Passwords don't match!";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}