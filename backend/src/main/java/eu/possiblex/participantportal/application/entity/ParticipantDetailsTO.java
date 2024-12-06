package eu.possiblex.participantportal.application.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDetailsTO extends ParticipantNameTO {
    private String participantEmail;
}
