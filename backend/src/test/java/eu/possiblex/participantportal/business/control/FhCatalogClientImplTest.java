package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = { FhCatalogClientImplTest.TestConfig.class, FhCatalogClientImpl.class })
class FhCatalogClientImplTest {

    @Autowired
    private SparqlFhCatalogClient sparqlFhCatalogClient;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClient;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void parseOfferDataCorrectly() throws OfferNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile("unit_tests/FHCatalogClientImplTest/validFhOffer.json");

        reset(technicalFhCatalogClient);
        reset(sparqlFhCatalogClient);

        Mockito.when(technicalFhCatalogClient.getFhCatalogOfferWithData(Mockito.anyString()))
            .thenReturn(fhCatalogOfferContent);

        // WHEN a dataset is retrieved

        PxExtendedServiceOfferingCredentialSubject offer = fhCatalogClient.getFhCatalogOffer("some ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertFalse(offer.getAggregationOf().isEmpty());
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
    }

    @Test
    void parseOfferDataCorrectlyNoDataResource() throws OfferNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile(
            "unit_tests/FHCatalogClientImplTest/validFhOfferNoDataResource.json");

        reset(technicalFhCatalogClient);
        reset(sparqlFhCatalogClient);

        WebClientResponseException expectedException = Mockito.mock(WebClientResponseException.class);
        Mockito.when(expectedException.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        Mockito.when(technicalFhCatalogClient.getFhCatalogOfferWithData(Mockito.anyString()))
            .thenThrow(expectedException);
        Mockito.when(technicalFhCatalogClient.getFhCatalogOffer(Mockito.anyString())).thenReturn(fhCatalogOfferContent);

        // WHEN a dataset is retrieved

        PxExtendedServiceOfferingCredentialSubject offer = fhCatalogClient.getFhCatalogOffer("some ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertNull(offer.getAggregationOf());
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
    }

    @Test
    void parseParticipantDataCorrectly() throws ParticipantNotFoundException {
        // GIVEN a mocked technical client that returns a test participant

        String participantContent = TestUtils.loadTextFile(
            "unit_tests/FHCatalogClientImplTest/validFhParticipant.json");

        TechnicalFhCatalogClient technicalFhCatalogClientMock = Mockito.mock(TechnicalFhCatalogClient.class);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogParticipant(Mockito.anyString()))
            .thenReturn(participantContent);
        FhCatalogClientImpl sut = new FhCatalogClientImpl(technicalFhCatalogClientMock, sparqlFhCatalogClient,
            new ObjectMapper());

        // WHEN a participant is retrieved

        PxExtendedLegalParticipantCredentialSubjectSubset participant = sut.getFhCatalogParticipant("some ID");

        // THEN the participant should contain the data parsed from the test participant

        Assertions.assertNotNull(participant);
        Assertions.assertEquals("Example", participant.getName());
        Assertions.assertEquals("This is an Example Org", participant.getDescription());
        Assertions.assertEquals("EXPECTED_MAIL_ADDRESS_VALUE", participant.getMailAddress());
    }

    @Test
    void parseSparqlDataOfferCorrectly() {

        String sparqlResponse = TestUtils.loadTextFile("unit_tests/FHCatalogClientImplTest/validSparqlResult.json");

        reset(technicalFhCatalogClient);
        reset(sparqlFhCatalogClient);
        Mockito.when(sparqlFhCatalogClient.queryCatalog(Mockito.anyString(), Mockito.isNull()))
            .thenReturn(sparqlResponse);

        Map<String, OfferingDetailsSparqlQueryResult> queryResultMap = fhCatalogClient.getOfferingDetails(
            List.of("EXPECTED_ASSET_ID_VALUE"));
        OfferingDetailsSparqlQueryResult queryResult = queryResultMap.get("EXPECTED_ASSET_ID_VALUE");

        verify(sparqlFhCatalogClient).queryCatalog(Mockito.anyString(), Mockito.isNull());
        Assertions.assertNotNull(queryResult);
        Assertions.assertTrue(!queryResultMap.isEmpty());
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", queryResult.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", queryResult.getProviderUrl());
        Assertions.assertEquals("EXPECTED_NAME_VALUE", queryResult.getName());
        Assertions.assertEquals("EXPECTED_DESCRIPTION_VALUE", queryResult.getDescription());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.mock(SparqlFhCatalogClient.class);
        }

        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            return Mockito.mock(TechnicalFhCatalogClient.class);
        }

        @Bean
        public ObjectMapper objectMapper() {

            return new ObjectMapper();
        }
    }
}
