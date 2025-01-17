package eu.possiblex.participantportal.application.configuration;

import eu.possiblex.participantportal.application.entity.ErrorResponseTO;
import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class BoundaryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(TransferFailedException e) {

        logError(e);
        String policyViolationMessage = getPolicyViolationMessage(e.getEnforcementPolicyList(), true);
        return new ResponseEntity<>(new ErrorResponseTO("Data transfer failed",
            policyViolationMessage.isEmpty() ? e.getMessage() : policyViolationMessage), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(ParticipantNotFoundException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Referenced participant was not found", e.getMessage()),
            NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(OfferNotFoundException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Offering with this id was not found", e.getMessage()),
            NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(OfferingComplianceException e) {

        logError(e);
        return new ResponseEntity<>(
            new ErrorResponseTO("Compliance was not attested for this offering", e.getMessage()), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(NegotiationFailedException e) {

        logError(e);
        String policyViolationMessage = getPolicyViolationMessage(e.getEnforcementPolicyList(), false);
        return new ResponseEntity<>(new ErrorResponseTO("Offer negotiation failed",
            policyViolationMessage.isEmpty() ? e.getMessage() : policyViolationMessage), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(FhOfferCreationException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Failed to create an offer on Piveau", e.getMessage()),
            UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(EdcOfferCreationException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Failed to create an offer on the EDC", e.getMessage()),
            UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(Exception e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("An unknown error occurred"), INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception e) {

        log.error("Caught boundary exception: {}", e.getClass().getName(), e);
    }

    private String getPolicyViolationMessage(List<EnforcementPolicy> enforcementPolicies, boolean isTransfer) {

        StringBuilder details = new StringBuilder();
        for (EnforcementPolicy enforcementPolicy : enforcementPolicies) {
            // if valid or everything allowed, this was not the cause
            if (enforcementPolicy.isValid() || enforcementPolicy instanceof EverythingAllowedPolicy) {
                continue;
            }

            // build string for a single policy violation
            StringBuilder policyDetails = new StringBuilder();
            // check policies relevant to negotiation
            if (enforcementPolicy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) {
                policyDetails.append("Participant is not in list of allowed organisations: [")
                    .append(String.join(", ", participantRestrictionPolicy.getAllowedParticipants())).append("]");
            } // check policies relevant to transfer as well
            else if (isTransfer && enforcementPolicy instanceof StartDatePolicy startDatePolicy) {
                policyDetails.append("Transfer is not allowed before: ").append(startDatePolicy.getDate());
            } else if (isTransfer && enforcementPolicy instanceof EndDatePolicy endDatePolicy) {
                policyDetails.append("Transfer is not allowed after: ").append(endDatePolicy.getDate());
            } else if (isTransfer
                && enforcementPolicy instanceof StartAgreementOffsetPolicy startAgreementOffsetPolicy) {
                policyDetails.append("Transfer is not allowed before ")
                    .append(startAgreementOffsetPolicy.getOffsetNumber())
                    .append(startAgreementOffsetPolicy.getOffsetUnit().toValue()).append(" after agreement");
            } else if (isTransfer && enforcementPolicy instanceof EndAgreementOffsetPolicy endAgreementOffsetPolicy) {
                policyDetails.append("Transfer is not allowed after ")
                    .append(endAgreementOffsetPolicy.getOffsetNumber())
                    .append(endAgreementOffsetPolicy.getOffsetUnit().toValue()).append(" after agreement");
            } else {  // unhandled policy
                policyDetails.append("Unknown enforcement policy violated: ")
                    .append(enforcementPolicy.getClass().getName());
            }

            if (!policyDetails.isEmpty()) {
                policyDetails.insert(0, "\t- ");
                policyDetails.append("\n");
                // add single policy violation to full message
                details.append(policyDetails);
            }
        }

        // add intro text if we have any violation messages
        if (!details.isEmpty()) {
            details.insert(0,
                "Your request cannot be processed because it does not comply with the specified policies. "
                    + "The following policy requirements are not fulfilled:\n");
        }

        return details.toString();
    }
}
