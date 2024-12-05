package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.TransferOfferRequestBE;
import eu.possiblex.participantportal.business.entity.TransferOfferResponseBE;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetDataAccountExport;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetTnC;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ContractServiceFake implements ContractService {

    public static final BigInteger DATE_IN_SECONDS = BigInteger.valueOf(1728549145);

    public static final String FAKE_ID_CONTRACT_AGREEMENT = "FAKE_ID_CONTRACT_AGREEMENT";

    public static final String FAKE_ID_PROVIDER = "FAKE_ID_PROVIDER";

    public static final String FAKE_ID_CONSUMER = "FAKE_ID_CONSUMER";

    public static final String FAKE_ID_ASSET = "FAKE_ID_ASSET";

    public static final String FAKE_ID_OFFERING = "FAKE_ID_OFFERING";

    public static final String NAME = "NAME";

    public static final String DESCRIPTION = "DESCRIPTION";

    public static OffsetDateTime getDateAsOffsetDateTime() {

        Instant instant = Instant.ofEpochSecond(DATE_IN_SECONDS.longValueExact());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("CET"));
        return zonedDateTime.toOffsetDateTime();
    }

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    @Override
    public List<ContractAgreementBE> getContractAgreements() {

        return getContractAgreementBEs();
    }

    private List<ContractAgreementBE> getContractAgreementBEs() {

        ContractAgreement contractAgreement = ContractAgreement.builder().contractSigningDate(DATE_IN_SECONDS)
            .id(FAKE_ID_CONTRACT_AGREEMENT).assetId(FAKE_ID_ASSET).consumerId(FAKE_ID_CONSUMER)
            .providerId(FAKE_ID_PROVIDER)
            .policy(Policy.builder().target(PolicyTarget.builder().id(FAKE_ID_ASSET).build()).build()).build();

        ContractAgreementBE contractAgreementBE = ContractAgreementBE.builder().contractAgreement(contractAgreement)
            .asset(getPossibleAsset(contractAgreement.getAssetId())).build();

        return List.of(contractAgreementBE);
    }

    private PossibleAsset getPossibleAsset(String assetId) {

        PossibleAssetDataAccountExport dataAccountExport = PossibleAssetDataAccountExport.builder()
            .accessType("digital").requestType("API").formatType("application/json").build();

        PossibleAssetTnC assetTnC = PossibleAssetTnC.builder().url("https://example.com").hash("hash1234").build();

        PossibleAssetProperties properties = PossibleAssetProperties.builder().termsAndConditions(List.of(assetTnC))
            .producedBy(new NodeKindIRITypeId(FAKE_ID_PROVIDER)).providedBy(new NodeKindIRITypeId(FAKE_ID_PROVIDER))
            .license(List.of("MIT")).copyrightOwnedBy(new NodeKindIRITypeId(FAKE_ID_PROVIDER))
            .exposedThrough(new NodeKindIRITypeId(FAKE_ID_OFFERING)).offerId(FAKE_ID_OFFERING).name(NAME)
            .description(DESCRIPTION).dataAccountExport(List.of(dataAccountExport)).build();

        Map<String, String> context = Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/", "odrl",
            "http://www.w3.org/ns/odrl/2/", "@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        IonosS3DataSource dataAddress = IonosS3DataSource.builder().bucketName("bucket").blobName(NAME).keyName(NAME)
            .region("storage").build();

        return PossibleAsset.builder().id(assetId).type("Asset").properties(properties).context(context)
            .dataAddress(dataAddress).build();
    }

    @Override
    public TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE request) {
        return null;
    }
}
