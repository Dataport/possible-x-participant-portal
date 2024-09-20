package eu.possiblex.participantportal.application.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = CreateServiceOfferingRequestTO.class, name = "service"),
    @JsonSubTypes.Type(value = CreateDataOfferingRequestTO.class, name = "data") })
public class CreateServiceOfferingRequestTO {

    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;

    private Policy policy;
}
