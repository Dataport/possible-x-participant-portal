package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.AssetDetailsTO;
import eu.possiblex.participantportal.application.entity.ContractAgreementTO;
import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
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
    @Mapping(target = "assetDetails", source = "be.asset")
    @Mapping(target = "policy", source = "be.contractAgreement.policy")
    @Mapping(target = "contractSigningDate", source = "be.contractAgreement.contractSigningDate", qualifiedByName = "secondsToOffsetDateTime")
    @Mapping(target = "consumerId", source = "be.contractAgreement.consumerId")
    @Mapping(target = "providerId", source = "be.contractAgreement.providerId")
    ContractAgreementTO contractAgreementBEToTO(ContractAgreementBE be);

    @Named("secondsToOffsetDateTime")
    default OffsetDateTime secondsToOffsetDateTime(BigInteger seconds) {

        Instant instant = Instant.ofEpochSecond(seconds.longValueExact());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("CET"));
        return zonedDateTime.toOffsetDateTime();
    }

    @Mapping(target = "name", source = "possibleAsset.properties.name")
    @Mapping(target = "description", source = "possibleAsset.properties.description")
    AssetDetailsTO possibleAssetToTO(PossibleAsset possibleAsset);
}
