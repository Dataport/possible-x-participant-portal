package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ContextConfiguration(classes = { ParticipantServiceTest.TestConfig.class, ParticipantServiceImpl.class })
class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Test
    void getParticipantIdSucceeds() {
        //when
        String participantId = participantService.getParticipantId();

        //then
        String expectedId = "did:web:test.eu";
        assertEquals(expectedId, participantId);
    }

    @Test
    void getParticipantDetailsSucceeds() throws ParticipantNotFoundException {

        // GIVEN

        reset(fhCatalogClient);

        // WHEN

        PxExtendedLegalParticipantCredentialSubjectSubset participant = new PxExtendedLegalParticipantCredentialSubjectSubset();
        participant.setName("Test Organization");
        participant.setMailAddress("test@org.eu");
        Mockito.when(fhCatalogClient.getFhCatalogParticipant("did:web:test.eu")).thenReturn(participant);

        PxExtendedLegalParticipantCredentialSubjectSubset response = participantService.getParticipantDetails();

        // THEN

        verify(fhCatalogClient, times(1)).getFhCatalogParticipant(any());

        assertNotNull(response);
    }

    @Test
    void getParticipantDetailsByIdSucceeds() throws ParticipantNotFoundException {

        // GIVEN

        reset(fhCatalogClient);

        // WHEN

        PxExtendedLegalParticipantCredentialSubjectSubset participant = new PxExtendedLegalParticipantCredentialSubjectSubset();
        participant.setName("Other Organization");
        participant.setMailAddress("other@org.com");
        Mockito.when(fhCatalogClient.getFhCatalogParticipant("did:web:other.com")).thenReturn(participant);

        PxExtendedLegalParticipantCredentialSubjectSubset response = participantService.getParticipantDetails("did:web:other.com");

        // THEN

        verify(fhCatalogClient, times(1)).getFhCatalogParticipant(any());

        assertNotNull(response);
    }

    @Test
    void getParticipantDetailsByIdNotFound() throws ParticipantNotFoundException {

        // GIVEN

        reset(fhCatalogClient);

        // WHEN

        ParticipantNotFoundException expectedException = Mockito.mock(ParticipantNotFoundException.class);
        Mockito.when(fhCatalogClient.getFhCatalogParticipant("did:web:unknown.com")).thenThrow(expectedException);

        assertThrows(ParticipantNotFoundException.class, () -> participantService.getParticipantDetails("did:web:unknown.com"));

        // THEN

        verify(fhCatalogClient, times(1)).getFhCatalogParticipant(any());
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @Bean
        public FhCatalogClient fhCatalogClient() {

            return Mockito.mock(FhCatalogClient.class);
        }
    }
}