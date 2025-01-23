package eu.possiblex.participantportal.business.entity.exception;

public class OfferingComplianceException extends RuntimeException {
    public OfferingComplianceException(String message) {

        super(message);
    }

    public OfferingComplianceException(String message, Throwable cause) {

        super(message, cause);
    }
}
