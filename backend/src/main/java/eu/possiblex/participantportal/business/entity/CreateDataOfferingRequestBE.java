package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxLegitimateInterest;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@SuperBuilder
public class CreateDataOfferingRequestBE extends CreateServiceOfferingRequestBE {
    private GxDataResourceCredentialSubject dataResource;

    private String fileName;

    private Optional<GxLegitimateInterest> legitimateInterest;
}
