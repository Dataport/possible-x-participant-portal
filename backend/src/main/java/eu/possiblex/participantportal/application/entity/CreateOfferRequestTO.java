package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.selfdescriptions.PojoCredentialSubject;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CreateOfferRequestTO {

    private List<PojoCredentialSubject> credentialSubjectList;

    private String fileName;

    private Policy policy;
}
