package eu.possiblex.participantportal.application.entity.validation;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LegitimateInterestValidator
    implements ConstraintValidator<ValidLegitimateInterestForPII, CreateDataOfferingRequestTO> {

    private String message;

    @Override
    public void initialize(ValidLegitimateInterestForPII constraintAnnotation) {

        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(CreateDataOfferingRequestTO request, ConstraintValidatorContext context) {

        // request must be non-null
        if (request == null) {
            return false;
        }

        if (request.getDataResourceCredentialSubject() != null && request.getDataResourceCredentialSubject()
            .isContainsPII() && request.getLegitimateInterestCredentialSubject() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.message).addPropertyNode("legitimateInterest")
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
