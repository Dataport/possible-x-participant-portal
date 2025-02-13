package eu.possiblex.participantportal.application.entity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LegitimateInterestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLegitimateInterestForPII {
    String message() default "Legitimate interest is required when data resource contains PII";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
