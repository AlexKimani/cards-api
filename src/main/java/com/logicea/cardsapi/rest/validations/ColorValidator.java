package com.logicea.cardsapi.rest.validations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ColorValidator.ColorValidatorImpl.class })
public @interface ColorValidator {
    String message() default "{com.logicea.cardsapi.rest.validations.ColorValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String regexp() default "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$";
    class ColorValidatorImpl implements ConstraintValidator<ColorValidator, String> {
        private Pattern pattern;
        /**
         * Initializes the validator in preparation for
         * {@link #isValid(Object, ConstraintValidatorContext)} calls.
         * The constraint annotation for a given constraint declaration
         * is passed.
         * <p>
         * This method is guaranteed to be called before any use of this instance for
         * validation.
         * <p>
         * The default implementation is a no-op.
         *
         * @param constraintAnnotation annotation instance for a given constraint declaration
         */
        @Override
        public void initialize(ColorValidator constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            this.pattern = Pattern.compile(constraintAnnotation.regexp());
        }

        /**
         * Implements the validation logic.
         * The state of {@code value} must not be altered.
         * <p>
         * This method can be accessed concurrently, thread-safety must be ensured
         * by the implementation.
         *
         * @param value   object to validate
         * @param context context in which the constraint is evaluated
         * @return {@code false} if {@code value} does not pass the constraint
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) return true;
            if (!value.startsWith("#")) return false;
            String subsetValue = value.substring(1);
            if (subsetValue.length() != 6) return false;
            Matcher matcher = this.pattern.matcher(subsetValue);
            return matcher.matches();
        }
    }
}
