/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
