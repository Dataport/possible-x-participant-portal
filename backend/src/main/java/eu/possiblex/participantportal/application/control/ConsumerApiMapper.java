package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOtoBE(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOtoBE(ConsumeOfferRequestTO to);

    TransferOfferRequestBE transferOfferRequestTOtoBE(TransferOfferRequestTO to);

    @Mapping(target = "edcOfferId", source = "edcOffer.assetId")
    @Mapping(target = "catalogOffering", source = "catalogOffering")
    @Mapping(target = "dataOffering", source = "dataOffering")
    @Mapping(target = "enforcementPolicies", source = "enforcementPolicies")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    AcceptOfferResponseTO acceptOfferResponseBEtoAcceptOfferResponseTO(AcceptOfferResponseBE acceptOfferResponseBE);

    TransferOfferResponseTO transferOfferResponseBEtoTransferOfferResponseTO(
        TransferOfferResponseBE transferOfferResponseBE);
}
