package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.AcceptOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.OfferDetailsTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
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
    @Mapping(target = "catalogOffering", source = "catalogOffering")
    @Mapping(target = "dataOffering", source = "dataOffering")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    AcceptOfferResponseTO acceptOfferResponseBEtoAcceptOfferResponseTO(AcceptOfferResponseBE acceptOfferResponseBE);
}
