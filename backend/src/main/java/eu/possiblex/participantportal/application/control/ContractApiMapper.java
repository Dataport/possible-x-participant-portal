package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.AssetDetailsTO;
import eu.possiblex.participantportal.application.entity.ContractAgreementTO;
import eu.possiblex.participantportal.application.entity.ContractParticipantDetailsTO;
import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.OfferingDetailsBE;
import eu.possiblex.participantportal.business.entity.ParticipantWithDapsBE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ContractApiMapper {
    @Mapping(target = "id", source = "be.contractAgreement.id")
    @Mapping(target = "assetId", source = "be.contractAgreement.assetId")
    @Mapping(target = "assetDetails", source = "be.offeringDetails")
    @Mapping(target = "policy", source = "be.contractAgreement.policy")
    @Mapping(target = "contractSigningDate", source = "be.contractAgreement.contractSigningDate", qualifiedByName = "secondsToOffsetDateTime")
    @Mapping(target = "consumerDetails", source = "be.consumerDetails")
    @Mapping(target = "providerDetails", source = "be.providerDetails")
    @Mapping(target = "isDataOffering", source = "be.dataOffering")
    @Mapping(target = "enforcementPolicies", source = "be.enforcementPolicies")
    ContractAgreementTO contractAgreementBEToTO(ContractAgreementBE be);

    @Named("secondsToOffsetDateTime")
    default OffsetDateTime secondsToOffsetDateTime(BigInteger seconds) {

        Instant instant = Instant.ofEpochSecond(seconds.longValueExact());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("CET"));
        return zonedDateTime.toOffsetDateTime();
    }

    AssetDetailsTO offeringDetailsBeToTO(OfferingDetailsBE offeringDetailsBE);

    ContractParticipantDetailsTO participantDetailsBEToTO(ParticipantWithDapsBE possibleParticipant);
}
