package eu.possiblex.participantportal.business.entity.exception;

public class CatalogContentParsingException extends RuntimeException {
    public CatalogContentParsingException(String message) {

        super(message);
    }

    public CatalogContentParsingException(String message, Throwable cause) {

        super(message, cause);
    }
}
