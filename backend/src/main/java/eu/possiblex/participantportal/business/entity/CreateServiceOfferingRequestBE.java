package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateServiceOfferingRequestBE {
    private NodeKindIRITypeId providedBy;

    private List<GxSOTermsAndConditions> termsAndConditions;

    private List<String> dataProtectionRegime;

    private List<GxDataAccountExport> dataAccountExport;

    private String name;

    private String description;

    private Policy policy;
}
