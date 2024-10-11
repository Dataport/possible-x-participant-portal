package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetDataAccountExport;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetTnC;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

@SpringBootTest
@ContextConfiguration(classes = { ContractServiceTest.TestConfig.class, ContractServiceImpl.class })
class ContractServiceTest {
    @Autowired
    private EdcClient edcClient;

    @Autowired
    private ContractService contractService;

    @Test
    void testGetContractAgreementsAsProviderOfAssets() {

        reset(edcClient);
        EdcClientFake.setProvider(true);

        List<ContractAgreementBE> expected = getContractAgreementBEsAsProviderOfAssets();
        List<ContractAgreementBE> actual = contractService.getContractAgreements();

        assertThat(actual.size()).isEqualTo(expected.size()).isEqualTo(1);
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected.get(0));

    }

    @Test
    void testGetContractAgreementsNotAsProviderOfAssets() {

        reset(edcClient);
        EdcClientFake.setProvider(false);

        List<ContractAgreementBE> expected = getContractAgreementBEsNotAsProviderOfAssets();
        List<ContractAgreementBE> actual = contractService.getContractAgreements();

        assertThat(actual.size()).isEqualTo(expected.size()).isEqualTo(1);
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected.get(0));

    }

    private List<ContractAgreementBE> getContractAgreementBEsAsProviderOfAssets() {

        ContractAgreement contractAgreement = getContractAgreement();

        ContractAgreementBE contractAgreementBE = ContractAgreementBE.builder().contractAgreement(contractAgreement)
            .asset(getPossibleAsset(contractAgreement.getAssetId())).build();

        return List.of(contractAgreementBE);
    }

    private List<ContractAgreementBE> getContractAgreementBEsNotAsProviderOfAssets() {

        ContractAgreement contractAgreement = getContractAgreement();

        ContractAgreementBE contractAgreementBE = ContractAgreementBE.builder().contractAgreement(contractAgreement)
            .asset(null).build();

        return List.of(contractAgreementBE);
    }

    private PossibleAsset getPossibleAsset(String assetId) {

        PossibleAssetDataAccountExport dataAccountExport = PossibleAssetDataAccountExport.builder()
            .accessType("digital").requestType("API").formatType("application/json").build();

        PossibleAssetTnC assetTnC = PossibleAssetTnC.builder().url("https://example.com").hash("hash1234").build();

        PossibleAssetProperties properties = PossibleAssetProperties.builder().termsAndConditions(List.of(assetTnC))
            .producedBy(new NodeKindIRITypeId(EdcClientFake.FAKE_ID))
            .providedBy(new NodeKindIRITypeId(EdcClientFake.FAKE_ID)).license(List.of("MIT"))
            .copyrightOwnedBy(new NodeKindIRITypeId(EdcClientFake.FAKE_ID))
            .exposedThrough(new NodeKindIRITypeId(EdcClientFake.FAKE_ID)).offerId(EdcClientFake.FAKE_ID).name("name")
            .description("description").dataAccountExport(List.of(dataAccountExport)).build();

        Map<String, String> context = Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/", "odrl",
            "http://www.w3.org/ns/odrl/2/", "@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        IonosS3DataSource dataAddress = IonosS3DataSource.builder().bucketName("bucket").blobName("name")
            .keyName("name").storage("storage").build();

        return PossibleAsset.builder().id(assetId).type("Asset").properties(properties).context(context)
            .dataAddress(dataAddress).build();
    }

    private ContractAgreement getContractAgreement() {

        return ContractAgreement.builder().contractSigningDate(BigInteger.valueOf(1728549145)).id(EdcClientFake.FAKE_ID)
            .assetId(EdcClientFake.FAKE_ID).consumerId(EdcClientFake.FAKE_ID).providerId(EdcClientFake.FAKE_ID)
            .policy(Policy.builder().target(PolicyTarget.builder().id(EdcClientFake.FAKE_ID).build()).build()).build();
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }
    }

}