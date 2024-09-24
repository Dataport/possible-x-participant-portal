package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@SuperBuilder
public class CreateDataOfferingRequestBE extends CreateServiceOfferingRequestBE {
    private GxDataResourceCredentialSubject dataResource;

    private String fileName;
}
