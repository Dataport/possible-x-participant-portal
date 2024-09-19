package eu.possiblex.participantportal.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class PossibleXException extends RuntimeException {

        private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        public PossibleXException(String message, HttpStatus status) {
            super(message);
            this.status = status;
        }

        public PossibleXException(String message) {
            super(message);
        }

        public PossibleXException(String message, HttpStatus status, Throwable cause) {
            super(message, cause);
            this.status = status;
        }
}
