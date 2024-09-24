package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderApiMapper {

    @Mapping(target = "policy", source = "policy")
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

}
