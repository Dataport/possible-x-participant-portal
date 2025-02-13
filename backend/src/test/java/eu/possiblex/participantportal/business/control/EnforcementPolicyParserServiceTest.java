package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.edc.policy.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = { EnforcementPolicyParserServiceTest.TestConfig.class,
    EnforcementPolicyParserServiceImpl.class })
class EnforcementPolicyParserServiceTest {

    @Autowired
    private EnforcementPolicyParserService sut;

    @Test
    void getEnforcementPolicyValid() {
        // when
        Policy enforcementPolicy = sut.getEverythingAllowedPolicy();

        // then
        assertNotNull(enforcementPolicy);
        assertEquals(2, enforcementPolicy.getPermission().size());
        OdrlPermission usePermission = enforcementPolicy.getPermission().get(0);
        assertEquals(OdrlAction.USE, usePermission.getAction());
        assertTrue(usePermission.getConstraint().isEmpty());

        OdrlPermission transferPermission = enforcementPolicy.getPermission().get(1);
        assertEquals(OdrlAction.USE, usePermission.getAction());
        assertTrue(transferPermission.getConstraint().isEmpty());
    }

    @Test
    void testParseEdcPoliciesToEnforcementPolicies() {
        // given

        List<Policy> edcPolicies = List.of(Policy.builder().permission(List.of(
            OdrlPermission.builder().action(OdrlAction.USE).target("http://example.com").constraint(List.of(
                OdrlConstraint.builder().leftOperand(ParticipantRestrictionPolicy.EDC_OPERAND).operator(OdrlOperator.IN)
                    .rightOperand("did:web:123,did:web:456").build(),
                OdrlConstraint.builder().leftOperand(TimeDatePolicy.EDC_OPERAND).operator(OdrlOperator.LEQ)
                    .rightOperand("2125-01-01T10:00:00+00:00").build(),
                OdrlConstraint.builder().leftOperand(TimeAgreementOffsetPolicy.EDC_OPERAND).operator(OdrlOperator.LEQ)
                    .rightOperand("contractAgreement+10d").build(),
                OdrlConstraint.builder().leftOperand(TimeDatePolicy.EDC_OPERAND).operator(OdrlOperator.GEQ)
                    .rightOperand("2025-01-01T10:00:00+00:00").build(),
                OdrlConstraint.builder().leftOperand(TimeAgreementOffsetPolicy.EDC_OPERAND).operator(OdrlOperator.GEQ)
                    .rightOperand("contractAgreement+5d").build())).build())).build());

        // when
        List<EnforcementPolicy> enforcementPolicies = sut.getEnforcementPoliciesFromEdcPolicies(edcPolicies);

        // then
        assertNotNull(enforcementPolicies);
        assertFalse(enforcementPolicies.isEmpty());
        assertEquals(5, enforcementPolicies.size());

        for (EnforcementPolicy enforcementPolicy : enforcementPolicies) {
            if (enforcementPolicy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) {
                assertIterableEquals(List.of("did:web:123", "did:web:456"),
                    participantRestrictionPolicy.getAllowedParticipants());
            } else if (enforcementPolicy instanceof StartAgreementOffsetPolicy startAgreementOffsetPolicy) {
                assertEquals(5, startAgreementOffsetPolicy.getOffsetNumber());
                assertEquals(AgreementOffsetUnit.DAYS, startAgreementOffsetPolicy.getOffsetUnit());
            } else if (enforcementPolicy instanceof EndAgreementOffsetPolicy endAgreementOffsetPolicy) {
                assertEquals(10, endAgreementOffsetPolicy.getOffsetNumber());
                assertEquals(AgreementOffsetUnit.DAYS, endAgreementOffsetPolicy.getOffsetUnit());
            } else if (enforcementPolicy instanceof StartDatePolicy startDatePolicy) {
                assertEquals(OffsetDateTime.parse("2025-01-01T10:00:00+00:00"), startDatePolicy.getDate());
            } else if (enforcementPolicy instanceof EndDatePolicy endDatePolicy) {
                assertEquals(OffsetDateTime.parse("2125-01-01T10:00:00+00:00"), endDatePolicy.getDate());
            } else {
                fail("Unexpected enforcement policy type");
            }
        }
    }

    @Test
    void testParseEdcPoliciesToEnforcementPoliciesAllInvalid() {
        // given\
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime signingDate = now.minusDays(7);
        BigInteger signingDateEpochSeconds = BigInteger.valueOf(signingDate.toEpochSecond());

        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("garbage")).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(10)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(5)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder().date(now.plusDays(3)).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(now.minusDays(3)).build();

        List<EnforcementPolicy> policies = List.of(participantRestrictionPolicy, startAgreementOffsetPolicy,
            endAgreementOffsetPolicy, startDatePolicy, endDatePolicy);

        List<Policy> edcPolicies = List.of(sut.getEdcPolicyFromEnforcementPolicies(policies));

        // when

        List<EnforcementPolicy> validatedPolicies = sut.getEnforcementPoliciesWithValidity(edcPolicies,
            signingDateEpochSeconds, "did:web:123");

        // then
        for (EnforcementPolicy p : validatedPolicies) {
            assertFalse(p.isValid());
        }

    }

    @Test
    void testParseEdcPoliciesToEnforcementPoliciesAllValid() {
        // given\
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime signingDate = now.minusDays(7);
        BigInteger signingDateEpochSeconds = BigInteger.valueOf(signingDate.toEpochSecond());

        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("did:web:123", "did:web:456", "did:web:test.eu")).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(5)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(10)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder().date(now.minusDays(3)).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(now.plusDays(3)).build();

        List<EnforcementPolicy> policies = List.of(participantRestrictionPolicy, startAgreementOffsetPolicy,
            endAgreementOffsetPolicy, startDatePolicy, endDatePolicy);

        List<Policy> edcPolicies = List.of(sut.getEdcPolicyFromEnforcementPolicies(policies));

        // when

        List<EnforcementPolicy> validatedPolicies = sut.getEnforcementPoliciesWithValidity(edcPolicies,
            signingDateEpochSeconds, "did:web:123");

        // then
        for (EnforcementPolicy p : validatedPolicies) {
            assertTrue(p.isValid());
        }

    }

    @Test
    void testParseEnforcementPoliciesToEdcPolicies() {
        // given
        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("did:web:123", "did:web:456")).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(5)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(10)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder()
            .date(OffsetDateTime.parse("2025-01-01T10:00:00+00:00")).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(OffsetDateTime.parse("2125-01-01T10:00:00+00:00"))
            .build();

        List<EnforcementPolicy> policies = List.of(participantRestrictionPolicy, startAgreementOffsetPolicy,
            endAgreementOffsetPolicy, startDatePolicy, endDatePolicy);

        // when
        Policy edcPolicy = sut.getEdcPolicyFromEnforcementPolicies(policies);

        // then
        int constraintCount = 0;
        // for access policy make sure there are no constraints
        for (OdrlPermission permission : edcPolicy.getPermission()) {
            if (permission.getAction().equals(OdrlAction.USE)) {  // currently only use is enforced
                continue;
            }
            for (OdrlConstraint constraint : permission.getConstraint()) {
                constraintCount++;
                if (constraint.getLeftOperand().equals(ParticipantRestrictionPolicy.EDC_OPERAND)) {
                    assertIterableEquals(participantRestrictionPolicy.getAllowedParticipants(),
                        Arrays.asList(constraint.getRightOperand().split(",")));
                } else if (constraint.getLeftOperand().equals(TimeAgreementOffsetPolicy.EDC_OPERAND)) {
                    boolean endDate = constraint.getOperator().equals(OdrlOperator.LEQ);
                    boolean offsetPolicy = constraint.getRightOperand().startsWith("contractAgreement+");

                    if (offsetPolicy) {
                        String offsetString = constraint.getRightOperand().substring("contractAgreement+".length());
                        int offsetNumber = Integer.parseInt(offsetString.substring(0, offsetString.length() - 1));
                        AgreementOffsetUnit offsetUnit = AgreementOffsetUnit.forValue(
                            offsetString.substring(offsetString.length() - 1));
                        if (endDate) {
                            assertEquals(endAgreementOffsetPolicy.getOffsetNumber(), offsetNumber);
                            assertEquals(endAgreementOffsetPolicy.getOffsetUnit(), offsetUnit);
                        } else {
                            assertEquals(startAgreementOffsetPolicy.getOffsetNumber(), offsetNumber);
                            assertEquals(startAgreementOffsetPolicy.getOffsetUnit(), offsetUnit);
                        }
                    } else {
                        if (endDate) {
                            assertEquals(endDatePolicy.getDate(), OffsetDateTime.parse(constraint.getRightOperand()));
                        } else {
                            assertEquals(startDatePolicy.getDate(), OffsetDateTime.parse(constraint.getRightOperand()));
                        }
                    }
                } else {
                    fail("Unexpected constraint: " + constraint);
                }
            }
        }
        assertEquals(policies.size(), constraintCount);
    }

    @TestConfiguration
    static class TestConfig {

    }
}
