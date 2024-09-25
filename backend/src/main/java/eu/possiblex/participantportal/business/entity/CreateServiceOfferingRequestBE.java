package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.NodeKindIRITypeId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@SuperBuilder
public class CreateServiceOfferingRequestBE {
    private NodeKindIRITypeId providedBy;

    private List<GxSOTermsAndConditions> termsAndConditions;

    private List<String> dataProtectionRegime;

    private List<GxDataAccountExport> dataAccountExport;

    private String name;

    private String description;

    private Policy policy;
}
