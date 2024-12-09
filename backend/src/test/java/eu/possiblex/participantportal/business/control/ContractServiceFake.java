package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.*;
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
            .offeringDetails(new OfferingDetailsBE(NAME, DESCRIPTION, FAKE_ID_ASSET, FAKE_ID_OFFERING))
            .providerDetails(new ParticipantDetailsBE())
            .consumerDetails(new ParticipantDetailsBE())
            .build();

        return List.of(contractAgreementBE);
    }

    @Override
    public TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE request) {
        return null;
    }
}
