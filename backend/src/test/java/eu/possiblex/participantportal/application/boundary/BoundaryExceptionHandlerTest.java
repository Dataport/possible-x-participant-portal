package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.configuration.BoundaryExceptionHandler;
import eu.possiblex.participantportal.application.entity.ErrorResponseTO;
import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoundaryExceptionHandlerTest {

    BoundaryExceptionHandler sut = new BoundaryExceptionHandler();

    @Test
    void testPolicyViolationMessageNegotiation() {

        // when
        ResponseEntity<ErrorResponseTO> response = sut.handleException(
            new NegotiationFailedException("negotiation failed", getViolatedPolicies()));

        // then
        String details = response.getBody().getDetails();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(details.contains("not in list"));
        assertFalse(details.contains("Unknown"));
        assertFalse(details.contains("not allowed before"));
        assertFalse(details.contains("not allowed after"));
    }

    @Test
    void testPolicyViolationMessageTransfer() {

        // when
        ResponseEntity<ErrorResponseTO> response = sut.handleException(
            new TransferFailedException("negotiation failed", getViolatedPolicies()));

        // then
        String details = response.getBody().getDetails();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(2, StringUtils.countMatches(details, "not allowed before"));
        assertEquals(2, StringUtils.countMatches(details, "not allowed after"));
        assertTrue(details.contains("not in list"));
        assertFalse(details.contains("Unknown"));
    }

    private List<EnforcementPolicy> getViolatedPolicies() {

        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("did:web:123")).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder().date(OffsetDateTime.now().plusDays(1)).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(OffsetDateTime.now().minusDays(1)).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(1)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(1)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();

        List<EnforcementPolicy> enforcementPolicies = List.of(participantRestrictionPolicy, startDatePolicy,
            endDatePolicy, startAgreementOffsetPolicy, endAgreementOffsetPolicy);
        for (EnforcementPolicy ep : enforcementPolicies) {
            ep.setValid(false);
        }
        return enforcementPolicies;
    }
}
