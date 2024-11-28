package eu.possiblex.participantportal.business.entity.exception;

public class SparqlQueryException extends RuntimeException{

        public SparqlQueryException(String message) {
            super(message);
        }

        public SparqlQueryException(String message, Throwable cause) {
            super(message, cause);
        }
}
