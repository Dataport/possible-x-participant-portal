package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.selfdescriptions.PojoCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderApiMapper {

    @Mapping(target = "assetName", source = "credentialSubjectList", qualifiedByName = "customOfferNameMapping")
    @Mapping(target = "assetDescription", source = "credentialSubjectList", qualifiedByName = "customOfferDescriptionMapping")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "policy", source = "policy")
    CreateEdcOfferBE getCreateEdcOfferDTOFromCreateOfferRequestTO(CreateOfferRequestTO createOfferRequestTO);

    @Mapping(target = "offerName", source = "credentialSubjectList", qualifiedByName = "customOfferNameMapping")
    @Mapping(target = "offerDescription", source = "credentialSubjectList", qualifiedByName = "customOfferDescriptionMapping")
    @Mapping(target = "policy", source = "policy")
    CreateFhOfferBE getCreateDatasetEntryDTOFromCreateOfferRequestTO(CreateOfferRequestTO createOfferRequestTO);

    @Named("customOfferNameMapping")
    default String customOfferNameMapping(List<PojoCredentialSubject> credentialSubjectList) {

        GxServiceOfferingCredentialSubject credentialSubject = CredentialSubjectUtils.findFirstCredentialSubjectByType(
            GxServiceOfferingCredentialSubject.class, credentialSubjectList);

        if (credentialSubject == null) {
            return "";
        }
        return credentialSubject.getName();
    }

    @Named("customOfferDescriptionMapping")
    default String customOfferDescriptionMapping(List<PojoCredentialSubject> credentialSubjectList) {

        GxServiceOfferingCredentialSubject credentialSubject = CredentialSubjectUtils.findFirstCredentialSubjectByType(
            GxServiceOfferingCredentialSubject.class, credentialSubjectList);

        if (credentialSubject == null) {
            return "";
        }
        return credentialSubject.getDescription();
    }
}
