/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.OfferRetrievalResponseBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.CatalogParsingException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = { FhCatalogClientTest.TestConfig.class, FhCatalogClientImpl.class })
class FhCatalogClientTest {

    @Autowired
    private SparqlFhCatalogClient sparqlFhCatalogClient;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClient;

    @Autowired
    private FhCatalogClient sut;

    @BeforeEach
    void setUp() {

        reset(technicalFhCatalogClient);
        reset(sparqlFhCatalogClient);
    }

    @Test
    void getFhCatalogOfferWithDataSuccess() {

        // WHEN

        OfferRetrievalResponseBE offerRetrievalResponseBE = sut.getFhCatalogOffer(
            TechnicalFhCatalogClientFake.VALID_FH_DATA_OFFER_ID);
        PxExtendedServiceOfferingCredentialSubject offer = offerRetrievalResponseBE.getCatalogOffering();

        // THEN

        assertNotNull(offer);
        assertNotNull(offer.getAggregationOf());
        assertFalse(offer.getAggregationOf().isEmpty());
        assertEquals("myId", offer.getAssetId());
        assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
        assertTrue(offerRetrievalResponseBE.getOfferRetrievalDate().isBefore(OffsetDateTime.now()));
    }

    @Test
    void getFhCatalogOfferSuccess() {

        // WHEN

        OfferRetrievalResponseBE offerRetrievalResponseBE = sut.getFhCatalogOffer(
            TechnicalFhCatalogClientFake.VALID_FH_SERVICE_OFFER_ID);
        PxExtendedServiceOfferingCredentialSubject offer = offerRetrievalResponseBE.getCatalogOffering();

        // THEN

        assertNotNull(offer);
        assertNull(offer.getAggregationOf());
        assertEquals("myId", offer.getAssetId());
        assertEquals("EXPECTED_PROVIDER_URL_VALUE", offer.getProviderUrl());
        assertTrue(offerRetrievalResponseBE.getOfferRetrievalDate().isBefore(OffsetDateTime.now()));
    }

    @Test
    void getFhCatalogOfferNotFound() {

        assertThrows(OfferNotFoundException.class,
            () -> sut.getFhCatalogOffer(TechnicalFhCatalogClientFake.MISSING_FH_SERVICE_OFFER_ID));
    }

    @Test
    void getFhCatalogOfferParsingError() {

        assertThrows(CatalogParsingException.class,
            () -> sut.getFhCatalogOffer(TechnicalFhCatalogClientFake.BAD_PARSING_ID));
    }

    @Test
    void getFhCatalogParticipantSuccess() {

        // WHEN

        PxExtendedLegalParticipantCredentialSubjectSubset participant = sut.getFhCatalogParticipant(
            TechnicalFhCatalogClientFake.VALID_FH_PARTICIPANT_ID);

        // THEN

        assertNotNull(participant);
        assertEquals("Example", participant.getName());
        assertEquals("This is an Example Org", participant.getDescription());
        assertEquals("test@possible.de", participant.getMailAddress());
    }

    @Test
    void getFhCatalogParticipantNotFound() {

        assertThrows(ParticipantNotFoundException.class,
            () -> sut.getFhCatalogParticipant(TechnicalFhCatalogClientFake.MISSING_FH_PARTICIPANT_ID));
    }

    @Test
    void getFhCatalogParticipantParsingError() {

        assertThrows(CatalogParsingException.class,
            () -> sut.getFhCatalogParticipant(TechnicalFhCatalogClientFake.BAD_PARSING_ID));
    }

    @Test
    void getOfferingDetailsByAssetIds() {

        // WHEN
        String offerId = "myId";
        Map<String, OfferingDetailsSparqlQueryResult> queryResultMap = sut.getOfferingDetailsByAssetIds(
            List.of(offerId));
        OfferingDetailsSparqlQueryResult queryResult = queryResultMap.get(offerId);

        // THEN
        verify(sparqlFhCatalogClient).queryCatalog(Mockito.anyString(), Mockito.isNull());
        assertNotNull(queryResult);
        assertFalse(queryResultMap.isEmpty());
        assertEquals(offerId, queryResult.getAssetId());
        assertEquals("https://example.com/protocol", queryResult.getProviderUrl());
        assertFalse(queryResult.getName().isEmpty());
        assertFalse(queryResult.getDescription().isEmpty());
    }

    @Test
    void getParticipantDetailsByIds() {

        String participantId = "did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076";

        Map<String, ParticipantDetailsSparqlQueryResult> queryResultMap = sut.getParticipantDetailsByIds(
            List.of(participantId));
        ParticipantDetailsSparqlQueryResult queryResult = queryResultMap.get(participantId);

        verify(sparqlFhCatalogClient).queryCatalog(Mockito.anyString(), Mockito.isNull());
        assertNotNull(queryResult);
        assertFalse(queryResultMap.isEmpty());
        Assertions.assertEquals(participantId, queryResult.getUri());
        Assertions.assertEquals("EXPECTED_NAME_VALUE", queryResult.getName());
        Assertions.assertEquals("EXPECTED_MAIL_ADDRESS_VALUE", queryResult.getMailAddress());
    }

    @Test
    void deleteServiceOfferingFromFhCatalog() {

        assertDoesNotThrow(() -> sut.deleteServiceOfferingFromFhCatalog(TechnicalFhCatalogClientFake.VALID_FH_SERVICE_OFFER_ID, true));
    }

    @Test
    void deleteServiceOfferingWithDataFromFhCatalog() {

        assertDoesNotThrow(() -> sut.deleteServiceOfferingFromFhCatalog(TechnicalFhCatalogClientFake.VALID_FH_DATA_OFFER_ID, true));
    }

    @Test
    void deleteServiceOfferingFromFhCatalogNotFound() {

        // WHEN

        sut.deleteServiceOfferingFromFhCatalog(TechnicalFhCatalogClientFake.MISSING_FH_SERVICE_OFFER_ID, false);

        // THEN

        verify(technicalFhCatalogClient).deleteServiceOfferingFromFhCatalog(
            TechnicalFhCatalogClientFake.MISSING_FH_SERVICE_OFFER_ID);
    }

    @Test
    void deleteServiceOfferingWithDataFromFhCatalogNotFound() {

        // WHEN

        sut.deleteServiceOfferingFromFhCatalog(TechnicalFhCatalogClientFake.MISSING_FH_DATA_OFFER_ID, true);

        // THEN

        verify(technicalFhCatalogClient).deleteServiceOfferingWithDataFromFhCatalog(
            TechnicalFhCatalogClientFake.MISSING_FH_DATA_OFFER_ID);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.spy(SparqlFhCatalogClientFake.class);
        }

        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            return Mockito.spy(TechnicalFhCatalogClientFake.class);
        }

        @Bean
        public ObjectMapper objectMapper() {

            return new ObjectMapper();
        }
    }
}
