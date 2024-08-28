package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProviderApiMapper {

    @Mapping(target = "assetName", source = "offerName")
    @Mapping(target = "assetDescription", source = "offerDescription")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "policy", source = "policy")
    CreateEdcOfferBE getCreateEdcOfferDTOFromCreateOfferRequestTO(CreateOfferRequestTO createOfferRequestTO);

    @Mapping(target = "policy", source = "policy")
    CreateFhOfferBE getCreateDatasetEntryDTOFromCreateOfferRequestTO(CreateOfferRequestTO createOfferRequestTO);
}
