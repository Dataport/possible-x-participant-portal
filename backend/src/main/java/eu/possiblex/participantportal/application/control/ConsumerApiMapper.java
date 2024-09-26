package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.entity.AcceptOfferResponseBE;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferResponseBE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOtoBE(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOtoBE(ConsumeOfferRequestTO to);

    @Mapping(target = "edcOfferId", source = "edcOffer.assetId")
    @Mapping(target = "offerType", constant = "Data Resource") // TODO pass actual data
    @Mapping(target = "creationDate", expression = "java(OffsetDateTime.now())") // TODO pass actual data
    @Mapping(target = "name", source = "edcOffer.name")
    @Mapping(target = "description", source = "edcOffer.description")
    @Mapping(target = "contentType", source = "edcOffer.contenttype")
    @Mapping(target = "counterPartyAddress", source = "counterPartyAddress")
    @Mapping(target = "dataOffering", source = "dataOffering")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    AcceptOfferResponseTO acceptOfferResponseBEtoAcceptOfferResponseTO(AcceptOfferResponseBE acceptOfferResponseBE);
}
