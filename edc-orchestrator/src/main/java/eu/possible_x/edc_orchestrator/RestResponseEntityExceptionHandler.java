package eu.possible_x.edc_orchestrator;

import eu.possible_x.edc_orchestrator.entities.ExceptionTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Logger;
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        ObjectMapper om = new ObjectMapper();
        String body = ex.getMessage();
        HttpStatus httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionTO exceptionTo = new ExceptionTO(httpStatusCode.value(), body);
        try {
            String exceptionToJson = om.writeValueAsString(exceptionTo);
            log.info(exceptionToJson);
            return new ResponseEntity<>(exceptionToJson,httpStatusCode);
        } catch (JsonProcessingException e) {
            log.info("could not convert exceptionTo to json");
            throw new IllegalCallerException();
        }
    }
}
