package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class FhCatalogClientImplTest {

    @Test
    void parseDataCorrectly() throws OfferNotFoundException, ParticipantNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile("unit_tests/FHCatalogClientImplTest/validFhOffer.json");
        String fhCatalogParticipant = TestUtils.loadTextFile(
            "unit_tests/FHCatalogClientImplTest/validFhParticipant.json");

        TechnicalFhCatalogClient technicalFhCatalogClientMock = Mockito.mock(TechnicalFhCatalogClient.class);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOfferWithData(Mockito.anyString()))
            .thenReturn(fhCatalogOfferContent);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogParticipant(Mockito.anyString()))
            .thenReturn(fhCatalogParticipant);
        FhCatalogClientImpl sut = new FhCatalogClientImpl(technicalFhCatalogClientMock, new ObjectMapper());

        // WHEN a dataset is retrieved

        PxExtendedServiceOfferingCredentialSubject offer = sut.getFhCatalogOffer("some ID");
        PxExtendedLegalParticipantCredentialSubjectSubset participant = sut.getFhCatalogParticipant("some participant ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertFalse(offer.getAggregationOf().isEmpty());
        Assertions.assertNotNull(participant);
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
        Assertions.assertEquals("EXPECTED_MAIL_ADDRESS_VALUE", participant.getMailAddress());
    }

    @Test
    void parseDataCorrectlyNoDataOffering() throws OfferNotFoundException, ParticipantNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile(
            "unit_tests/FHCatalogClientImplTest/validFhOfferNoDataResource.json");
        String fhCatalogParticipant = TestUtils.loadTextFile(
            "unit_tests/FHCatalogClientImplTest/validFhParticipant.json");

        TechnicalFhCatalogClient technicalFhCatalogClientMock = Mockito.mock(TechnicalFhCatalogClient.class);
        WebClientResponseException expectedException = Mockito.mock(WebClientResponseException.class);
        Mockito.when(expectedException.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOfferWithData(Mockito.anyString()))
                .thenThrow(expectedException);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.anyString()))
            .thenReturn(fhCatalogOfferContent);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogParticipant(Mockito.anyString()))
            .thenReturn(fhCatalogParticipant);
        FhCatalogClientImpl sut = new FhCatalogClientImpl(technicalFhCatalogClientMock, new ObjectMapper());

        // WHEN a dataset is retrieved

        PxExtendedServiceOfferingCredentialSubject offer = sut.getFhCatalogOffer("some ID");
        PxExtendedLegalParticipantCredentialSubjectSubset participant = sut.getFhCatalogParticipant("some participant ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertNotNull(participant);
        Assertions.assertNull(offer.getAggregationOf());
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
        Assertions.assertEquals("EXPECTED_MAIL_ADDRESS_VALUE", participant.getMailAddress());
    }
}
