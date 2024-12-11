package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOToBE(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOToBE(ConsumeOfferRequestTO to);

    TransferOfferRequestBE transferOfferRequestTOToBE(TransferOfferRequestTO to);

    @Mapping(target = "edcOfferId", source = "edcOffer.assetId")
    @Mapping(target = "catalogOffering", source = "catalogOffering")
    @Mapping(target = "dataOffering", source = "dataOffering")
    @Mapping(target = "enforcementPolicies", source = "enforcementPolicies")
    @Mapping(target = "providerDetails", source = "providerDetails")
    @Mapping(target = "participantNames", source = "participantNames")
    @Mapping(target = "offerRetrievalDate", source = "offerRetrievalDate")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    AcceptOfferResponseTO acceptOfferResponseBEToAcceptOfferResponseTO(AcceptOfferResponseBE acceptOfferResponseBE);

    TransferOfferResponseTO transferOfferResponseBEToTransferOfferResponseTO(
        TransferOfferResponseBE transferOfferResponseBE);

    @Mapping(target = "participantId", source = "did")
    @Mapping(target = "participantName", source = "name")
    ParticipantNameTO participantNameBEToParticipantNameTO(ParticipantNameBE participantNameBE);

    @Mapping(target = "participantId", source = "did")
    @Mapping(target = "participantName", source = "name")
    @Mapping(target = "participantEmail", source = "mailAddress")
    ParticipantDetailsTO participantWithMailBEToParticipantWithMailTO(ParticipantWithMailBE participantWithMailBE);
}
