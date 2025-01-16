package eu.possiblex.participantportal.application.configuration;

import eu.possiblex.participantportal.application.entity.ErrorResponseTO;
import eu.possiblex.participantportal.business.entity.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class BoundaryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseTO> handleException(TransferFailedException e) {

        logError(e);
        return new ResponseEntity<>(new ErrorResponseTO("Data transfer failed", e.getMessage()), UNPROCESSABLE_ENTITY);
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
        return new ResponseEntity<>(new ErrorResponseTO("Offer negotiation failed", e.getMessage()),
            UNPROCESSABLE_ENTITY);
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
}
