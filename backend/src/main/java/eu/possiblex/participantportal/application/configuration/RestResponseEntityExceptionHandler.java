package eu.possiblex.participantportal.application.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        log.error("Caught boundary exception:", ex);
        throw ex;
    }
}
