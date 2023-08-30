package com.logicea.cardsapi.rest.validations;

import com.logicea.cardsapi.core.enums.CardStatus;
import jakarta.validation.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = EnumValidator.EnumValidatorImpl.class )
public @interface EnumValidator {
    String message() default "{com.logicea.cardsapi.rest.validations.EnumValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends  Enum<?>> targetClassType();

    class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
        List<String> valueList = null;
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
        @SuppressWarnings({
                "rawtypes"
        })
        @Override
        public void initialize(EnumValidator constraintAnnotation) {
            this.valueList = new ArrayList<>();
            Class<? extends Enum<?>> enumClass = constraintAnnotation.targetClassType();

            Enum[] enumValArr = enumClass.getEnumConstants();

            Arrays.stream(enumValArr).forEach(enumVal -> this.valueList.add(enumVal.name().toUpperCase()));
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
            return valueList.contains(CardStatus.fromString(value).toString());
        }
    }
}
