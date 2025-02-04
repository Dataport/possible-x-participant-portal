package eu.possiblex.participantportal.application.entity.validation;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LegitimateInterestValidator  implements
    ConstraintValidator<ValidLegitimateInterestForPII, CreateDataOfferingRequestTO> {

    @Override
    public boolean isValid(CreateDataOfferingRequestTO request, ConstraintValidatorContext context) {
        if (request.getDataResourceCredentialSubject() != null && request.getDataResourceCredentialSubject().isContainsPII()) {
            return request.getLegitimateInterest() != null;
        }
        return true;
    }
}
