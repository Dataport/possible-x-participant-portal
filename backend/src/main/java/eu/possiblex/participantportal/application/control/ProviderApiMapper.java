package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhServiceOfferingBE;
import eu.possiblex.participantportal.business.entity.fh.catalog.CreateFhDataOfferingBE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderApiMapper {

    @Mapping(target = "assetName", source = "serviceOfferingCredentialSubject.name")
    @Mapping(target = "assetDescription", source = "serviceOfferingCredentialSubject.description")
    @Mapping(target = "fileName", constant = "")
    @Mapping(target = "policy", source = "policy")
    CreateEdcOfferBE getCreateEdcOfferBE(CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    @Mapping(target = "assetName", source = "serviceOfferingCredentialSubject.name")
    @Mapping(target = "assetDescription", source = "serviceOfferingCredentialSubject.description")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "policy", source = "policy")
    CreateEdcOfferBE getCreateEdcOfferBE(CreateDataOfferingRequestTO createDataOfferingRequestTO);

    @Mapping(target = "serviceOfferingCredentialSubject", source = "serviceOfferingCredentialSubject")
    CreateFhServiceOfferingBE getCreateFhServiceOfferingBE(
        CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    @Mapping(target = "serviceOfferingCredentialSubject", source = "serviceOfferingCredentialSubject")
    @Mapping(target = "dataResourceCredentialSubject", source = "dataResourceCredentialSubject")
    CreateFhDataOfferingBE getCreateFhDataOfferingBE(CreateDataOfferingRequestTO createDataOfferingRequestTO);
}
