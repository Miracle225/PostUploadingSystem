package com.example.MiniSocialNetwork.annotations;


import com.example.MiniSocialNetwork.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Password do not matches";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
