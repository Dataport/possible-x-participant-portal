package eu.possiblex.participantportal.business.entity.edc.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuerySpec {
    private List<FilterExpression> filterExpression;

    @Builder.Default
    private int limit = 50;

    private int offset;
}
