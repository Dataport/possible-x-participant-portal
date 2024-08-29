package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ExceptionTO {
    private int httpStatusCode;
    private String message;
}
