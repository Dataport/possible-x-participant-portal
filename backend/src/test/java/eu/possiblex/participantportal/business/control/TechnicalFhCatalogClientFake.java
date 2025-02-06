package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.utils.TestUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class TechnicalFhCatalogClientFake implements TechnicalFhCatalogClient {

    public static final String VALID_FH_DATA_OFFER_ID = "validFhCatalogDataOfferId";

    public static final String VALID_FH_SERVICE_OFFER_ID = "validFhCatalogServiceOfferId";

    private final String fhCatalogDataOfferContent;

    private final String fhCatalogServiceOfferContent;

    public TechnicalFhCatalogClientFake() {

        this.fhCatalogDataOfferContent = TestUtils.loadTextFile("unit_tests/ConsumerModuleTest/validFhOffer.json");
        this.fhCatalogServiceOfferContent = TestUtils.loadTextFile(
            "unit_tests/ConsumerModuleTest/validFhOfferWithoutData.json");
    }

    @Override
    public FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCs, String id, String verificationMethod) {

        return new FhCatalogIdResponse(id);
    }

    @Override
    public FhCatalogIdResponse addServiceOfferingWithDataToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCs, String id, String verificationMethod) {

        return new FhCatalogIdResponse(id);
    }

    @Override
    public String getFhCatalogOffer(String offeringId) {

        if (offeringId.equals(VALID_FH_SERVICE_OFFER_ID)) {
            return this.fhCatalogServiceOfferContent;
        }
        throw new WebClientResponseException(404, "not found", null, null, null);
    }

    @Override
    public String getFhCatalogParticipant(String participantId) {

        return "";
    }

    @Override
    public String getFhCatalogOfferWithData(String offeringId) {

        if (offeringId.equals(VALID_FH_DATA_OFFER_ID)) {
            return this.fhCatalogDataOfferContent;
        }
        throw new WebClientResponseException(404, "not found", null, null, null);
    }

    @Override
    public void deleteServiceOfferingFromFhCatalog(String offeringId) {

    }

    @Override
    public void deleteServiceOfferingWithDataFromFhCatalog(String offeringId) {

    }
}
