package eu.possiblex.participantportal.business.entity.edc.catalog;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class FilterExpression {
    private String operandLeft;

    private String operator;

    private String operandRight;

}