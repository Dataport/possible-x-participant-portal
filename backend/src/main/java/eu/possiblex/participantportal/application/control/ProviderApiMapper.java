package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderApiMapper {

    @Mapping(target = "enforcementPolicy", source = "enforcementPolicy", qualifiedByName = "enforcementPolicyMapper")
    @Mapping(target = "name", source = "serviceOfferingCredentialSubject.name")
    @Mapping(target = "description", source = "serviceOfferingCredentialSubject.description")
    @Mapping(target = "providedBy", source = "serviceOfferingCredentialSubject.providedBy")
    @Mapping(target = "termsAndConditions", source = "serviceOfferingCredentialSubject.termsAndConditions")
    @Mapping(target = "dataProtectionRegime", source = "serviceOfferingCredentialSubject.dataProtectionRegime")
    @Mapping(target = "dataAccountExport", source = "serviceOfferingCredentialSubject.dataAccountExport")
    CreateServiceOfferingRequestBE getCreateOfferingRequestBE(
        CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    @InheritConfiguration
    @Mapping(target = "dataResource", source = "dataResourceCredentialSubject")
    @Mapping(target = "fileName", source = "fileName")
    CreateDataOfferingRequestBE getCreateOfferingRequestBE(CreateDataOfferingRequestTO createDataOfferingRequestTO);

    @Named("enforcementPolicyMapper")
    default List<EnforcementPolicy> enforcementPolicyMapper(List<EnforcementPolicy> enforcementPolicies) {

        if (enforcementPolicies.isEmpty()) {
            return List.of(new EverythingAllowedPolicy());
        } else {
            return enforcementPolicies;
        }
    }

}
