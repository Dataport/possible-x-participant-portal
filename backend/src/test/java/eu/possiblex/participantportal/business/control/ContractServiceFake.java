package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.TermsAndConditions;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ContractServiceFake implements ContractService {

    public static final BigInteger DATE_IN_SECONDS = BigInteger.valueOf(1728549145);

    public static final String FAKE_ID_CONTRACT_AGREEMENT = "FAKE_ID_CONTRACT_AGREEMENT";

    public static final String FAKE_ID_PROVIDER = "FAKE_ID_PROVIDER";

    public static final String FAKE_ID_CONSUMER = "FAKE_ID_CONSUMER";

    public static final String FAKE_ID_ASSET = "FAKE_ID_ASSET";

    public static final String FAKE_ID_OFFERING = "FAKE_ID_OFFERING";

    public static final String NAME = "NAME";

    public static final String DESCRIPTION = "DESCRIPTION";

    public static final String URL = "URL";

    public static final String HASH = "HASH";

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

    @Override
    public ContractAgreementBE getContractAgreementById(String contractAgreementId) throws OfferNotFoundException {

        return getContractAgreementBEs().get(0);
    }

    private List<ContractAgreementBE> getContractAgreementBEs() {

        ContractAgreement contractAgreement = ContractAgreement.builder().contractSigningDate(DATE_IN_SECONDS)
            .id(FAKE_ID_CONTRACT_AGREEMENT).assetId(FAKE_ID_ASSET).consumerId(FAKE_ID_CONSUMER)
            .providerId(FAKE_ID_PROVIDER)
            .policy(Policy.builder().target(PolicyTarget.builder().id(FAKE_ID_ASSET).build()).build()).build();

        ContractAgreementBE contractAgreementBE = ContractAgreementBE.builder().contractAgreement(contractAgreement)
            .offeringDetails(new OfferingDetailsBE(NAME, DESCRIPTION, FAKE_ID_ASSET, FAKE_ID_OFFERING,
                List.of(new TermsAndConditions(URL, HASH)))).providerDetails(new ParticipantWithDapsBE())
            .consumerDetails(new ParticipantWithDapsBE()).build();

        return List.of(contractAgreementBE);
    }

    @Override
    public TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE request) {

        return null;
    }
}
