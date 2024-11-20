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

    ContractPartiesRequestBE contractPartiesRequestTOToBE(ContractPartiesRequestTO to);

    @Mapping(target = "edcOfferId", source = "edcOffer.assetId")
    @Mapping(target = "catalogOffering", source = "catalogOffering")
    @Mapping(target = "dataOffering", source = "dataOffering")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    AcceptOfferResponseTO acceptOfferResponseBEToAcceptOfferResponseTO(AcceptOfferResponseBE acceptOfferResponseBE);

    TransferOfferResponseTO transferOfferResponseBEToTransferOfferResponseTO(
        TransferOfferResponseBE transferOfferResponseBE);

    @Mapping(target = "consumerDetails.participantId", source = "consumer.id")
    @Mapping(target = "consumerDetails.participantName", source = "consumer.name")
    @Mapping(target = "providerDetails.participantId", source = "provider.id")
    @Mapping(target = "providerDetails.participantName", source = "provider.name")
    ContractPartiesTO contractPartiesBEToContractPartiesTO(ContractPartiesBE contractPartiesBE);
}
