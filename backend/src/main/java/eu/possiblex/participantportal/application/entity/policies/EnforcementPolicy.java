package eu.possiblex.participantportal.application.entity.policies;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @JsonSubTypes.Type(value = EverythingAllowedPolicy.class, name = "EverythingAllowedPolicy"),
    @JsonSubTypes.Type(value = ParticipantRestrictionPolicy.class, name = "ParticipantRestrictionPolicy"), })
@EqualsAndHashCode
public abstract class EnforcementPolicy {
}
