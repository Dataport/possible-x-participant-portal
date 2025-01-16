package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseTO {
    private OffsetDateTime timestamp;

    private String message;

    private String details;

    public ErrorResponseTO(String message) {

        this.timestamp = OffsetDateTime.now();
        this.message = message;
        this.details = "";
    }

    public ErrorResponseTO(String message, String details) {

        this.timestamp = OffsetDateTime.now();
        this.message = message;
        this.details = details;
    }
}
