package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Mapping(target = "consumerDetails.participantId", source = "consumer.id", qualifiedByName = "extractParticipantDidWeb")
    @Mapping(target = "consumerDetails.participantName", source = "consumer.name")
    @Mapping(target = "providerDetails.participantId", source = "provider.id", qualifiedByName = "extractParticipantDidWeb")
    @Mapping(target = "providerDetails.participantName", source = "provider.name")
    ContractPartiesTO contractPartiesBEToContractPartiesTO(ContractPartiesBE contractPartiesBE);

    @Named("extractParticipantDidWeb")
    default String extractParticipantDid(String id) {

        String didWebRegex = "did:web.*";

        Pattern pattern = Pattern.compile(didWebRegex);
        Matcher matcher = pattern.matcher(id);

        String extracted = "";
        if (matcher.find()) {
            extracted = matcher.group();
        }
        return extracted;
    }
}
