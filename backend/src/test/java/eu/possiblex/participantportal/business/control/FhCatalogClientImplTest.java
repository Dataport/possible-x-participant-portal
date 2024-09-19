package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class FhCatalogClientImplTest {

    @Test
    public void parseDataCorrectly() throws OfferNotFoundException {
        // GIVEN a mocked technical client that returns a test FH Catalog offer

        String fhCatalogOfferContent = TestUtils.loadTextFile("unit_tests/FHCatalogClientImplTest/validFhOffer.json");

        TechnicalFhCatalogClient technicalFhCatalogClientMock = Mockito.mock(TechnicalFhCatalogClient.class);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.anyString())).thenReturn(fhCatalogOfferContent);
        FhCatalogClientImpl sut = new FhCatalogClientImpl(technicalFhCatalogClientMock);

        // WHEN a dataset is retrieved

        FhCatalogOffer offer = sut.getFhCatalogOffer("some ID");

        // THEN the offer should contain the data parsed from the test FH Catalog offer

        Assertions.assertNotNull(offer);
        Assertions.assertEquals("EXPECTED_ASSET_ID_VALUE", offer.getAssetId());
        Assertions.assertEquals("EXPECTED_ACCESS_URL_VALUE", offer.getCounterPartyAddress());
    }
}
