package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class FhCatalogClientImplTest {

    @Test
    void parseDataCorrectly() throws OfferNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile("unit_tests/FHCatalogClientImplTest/validFhOffer.json");

        TechnicalFhCatalogClient technicalFhCatalogClientMock = Mockito.mock(TechnicalFhCatalogClient.class);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOfferWithData(Mockito.anyString()))
            .thenReturn(fhCatalogOfferContent);
        FhCatalogClientImpl sut = new FhCatalogClientImpl(technicalFhCatalogClientMock, new ObjectMapper());

        // WHEN a dataset is retrieved

        PxExtendedServiceOfferingCredentialSubject offer = sut.getFhCatalogOffer("some ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertFalse(offer.getAggregationOf().isEmpty());
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
    }

    @Test
    void parseDataCorrectlyNoDataOffering() throws OfferNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile(
            "unit_tests/FHCatalogClientImplTest/validFhOfferNoDataResource.json");

        TechnicalFhCatalogClient technicalFhCatalogClientMock = Mockito.mock(TechnicalFhCatalogClient.class);
        WebClientResponseException expectedException = Mockito.mock(WebClientResponseException.class);
        Mockito.when(expectedException.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOfferWithData(Mockito.anyString()))
                .thenThrow(expectedException);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.anyString()))
            .thenReturn(fhCatalogOfferContent);
        FhCatalogClientImpl sut = new FhCatalogClientImpl(technicalFhCatalogClientMock, new ObjectMapper());

        // WHEN a dataset is retrieved

        PxExtendedServiceOfferingCredentialSubject offer = sut.getFhCatalogOffer("some ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertNull(offer.getAggregationOf());
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
    }
}
