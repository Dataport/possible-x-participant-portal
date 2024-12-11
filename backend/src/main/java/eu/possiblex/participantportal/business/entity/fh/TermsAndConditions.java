package eu.possiblex.participantportal.business.entity.fh;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TermsAndConditions {
    private String url;

    private String hash;
}
