package eu.possiblex.participantportal.application.configuration;

import eu.possiblex.participantportal.application.entity.ErrorResponseTO;
import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

/**
 * Exception handler for boundary exceptions that should be passed to the frontend.
 */
@RestControllerAdvice
@Slf4j
public class BoundaryExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle exceptions that occur when an EDC transfer fails.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(TransferFailedException e) {

        logError(e);
        String policyViolationMessage = getPolicyViolationMessage(e.getEnforcementPolicyList(), true);
        return new ResponseEntity<>(new ErrorResponseTO("Data transfer failed",
            policyViolationMessage.isEmpty() ? e.getMessage() : policyViolationMessage), UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle exceptions that occur when a referenced participant is not found.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(ParticipantNotFoundException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Referenced participant was not found", e.getMessage()),
            NOT_FOUND);
    }

    /**
     * Handle exceptions that occur when an offering is not found.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(OfferNotFoundException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Offering with this id was not found", e.getMessage()),
            NOT_FOUND);
    }

    /**
     * Handle exceptions that occur when a contract agreement is not found.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(ContractAgreementNotFoundException e) {

        logError(e);
        return new ResponseEntity<>(
            new ErrorResponseTO("Contract agreement with this id was not found", e.getMessage()), NOT_FOUND);
    }

    /**
     * Handle exceptions that occur when an offering being published is not compliant.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(OfferingComplianceException e) {

        logError(e);
        return new ResponseEntity<>(
            new ErrorResponseTO("Compliance was not attested for this offering", e.getMessage()), UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle exceptions that occur when an offer negotiation fails.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(NegotiationFailedException e) {

        logError(e);
        String policyViolationMessage = getPolicyViolationMessage(e.getEnforcementPolicyList(), false);
        return new ResponseEntity<>(new ErrorResponseTO("Offer negotiation failed",
            policyViolationMessage.isEmpty() ? e.getMessage() : policyViolationMessage), UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle exceptions that occur when an offer creation on the catalog fails.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(FhOfferCreationException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Failed to create an offer on Piveau", e.getMessage()),
            UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle exceptions that occur when an offer creation on the EDC fails.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(EdcOfferCreationException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Failed to create an offer on the EDC", e.getMessage()),
            UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(Exception e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("An unknown error occurred", ""), INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Spring validation exceptions.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        logError(ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        StringBuilder message = new StringBuilder();
        errors.forEach((key, value) -> message.append(key).append(": ").append(value).append("; "));

        return new ResponseEntity<>(new ErrorResponseTO("Request contained errors.", message.toString().strip()),
            BAD_REQUEST);
    }

    private void logError(Exception e) {

        log.error("Caught boundary exception: {}", e.getClass().getName(), e);
    }

    /**
     * If policies are involved in the exception, build a message with the violated policies.
     *
     * @param enforcementPolicies list of policies that were potentially violated
     * @param isTransfer whether the exception occurred during a transfer
     * @return message with violated policies
     */
    private String getPolicyViolationMessage(List<EnforcementPolicy> enforcementPolicies, boolean isTransfer) {

        StringBuilder details = new StringBuilder();
        for (EnforcementPolicy enforcementPolicy : enforcementPolicies) {
            // if valid or everything allowed, this was not the cause
            if (enforcementPolicy.isValid() || enforcementPolicy instanceof EverythingAllowedPolicy) {
                continue;
            }

            StringBuilder policyDetails = getSpecificPolicyViolationMessage(enforcementPolicy, isTransfer);

            if (!policyDetails.isEmpty()) {
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

    private StringBuilder getSpecificPolicyViolationMessage(EnforcementPolicy enforcementPolicy, boolean isTransfer) {
        // build string for a single policy violation
        StringBuilder policyDetails = new StringBuilder();
        // check policies relevant to negotiation
        if (enforcementPolicy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) {
            policyDetails.append("Participant is not in list of allowed organisations: [")
                .append(String.join(", ", participantRestrictionPolicy.getAllowedParticipants())).append("]");
        } // check policies relevant to transfer as well
        else if (enforcementPolicy instanceof StartDatePolicy startDatePolicy) {
            policyDetails.append("Negotiation/Transfer is not allowed before: ").append(startDatePolicy.getDate());
        } else if (enforcementPolicy instanceof EndDatePolicy endDatePolicy) {
            policyDetails.append("Negotiation/Transfer is not allowed after: ").append(endDatePolicy.getDate());
        } else if (enforcementPolicy instanceof StartAgreementOffsetPolicy startAgreementOffsetPolicy) {
            if (isTransfer) {
                policyDetails.append("Transfer is not allowed before ")
                    .append(startAgreementOffsetPolicy.getOffsetNumber())
                    .append(startAgreementOffsetPolicy.getOffsetUnit().toValue()).append(" after agreement");
            }
        } else if (enforcementPolicy instanceof EndAgreementOffsetPolicy endAgreementOffsetPolicy) {
            if (isTransfer) {
                policyDetails.append("Transfer is not allowed after ")
                    .append(endAgreementOffsetPolicy.getOffsetNumber())
                    .append(endAgreementOffsetPolicy.getOffsetUnit().toValue()).append(" after agreement");
            }
        } else {  // unhandled policy
            policyDetails.append("Unknown enforcement policy violated: ")
                .append(enforcementPolicy.getClass().getName());
        }

        // pad with bullet points and newline if we have a message
        if (!policyDetails.isEmpty()) {
            policyDetails.insert(0, "\t- ");
            policyDetails.append("\n");
        }
        return policyDetails;
    }
}
