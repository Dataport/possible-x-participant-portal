package eu.possiblex.participantportal.business.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantWithMailBE extends ParticipantNameBE{
    private String mailAddress;
}
