package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.edc.policy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EnforcementPolicyParserServiceImpl implements EnforcementPolicyParserService {

    private final String participantId;

    public EnforcementPolicyParserServiceImpl(@Value("${participant-id}") String participantId) {

        this.participantId = participantId;
    }

    /**
     * Given the ODRL Policy stored in the EDC, build the corresponding list of enforcement policies.
     *
     * @param policies ODRL Policies
     * @return enforcement policies
     */
    @Override
    public List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies) {

        List<OdrlConstraint> constraints = new ArrayList<>();
        for (Policy policy : policies) {
            for (OdrlPermission permission : policy.getPermission()) {
                constraints.addAll(permission.getConstraint());
            }
        }

        Set<EnforcementPolicy> enforcementPolicies = new HashSet<>();
        for (OdrlConstraint constraint : constraints) {
            EnforcementPolicy policy = switch (constraint.getLeftOperand()) {
                case ParticipantRestrictionPolicy.EDC_OPERAND -> parseParticipantRestrictionPolicy(constraint);
                case
                    TimeAgreementOffsetPolicy.EDC_OPERAND  // currently time agreement offset and time date have the same name in the edc, hence we need to handle both here
                    -> parseTimedEnforcementPolicy(constraint);
                default -> null;
            };

            if (policy != null) {
                enforcementPolicies.add(policy);
            } else {
                log.warn("Unknown enforcement policy: {}", constraint);
            }
        }

        if (enforcementPolicies.isEmpty()) {
            enforcementPolicies.add(new EverythingAllowedPolicy());
        }

        return enforcementPolicies.stream().toList();
    }

    private ParticipantRestrictionPolicy parseParticipantRestrictionPolicy(OdrlConstraint constraint) {

        return new ParticipantRestrictionPolicy(List.of(constraint.getRightOperand().split(",")));
    }

    private EnforcementPolicy parseTimedEnforcementPolicy(OdrlConstraint constraint) {
        // check whether we have a start or end date policy
        boolean endDate;
        if (constraint.getOperator().equals(OdrlOperator.LEQ)) {
            endDate = true;
        } else if (constraint.getOperator().equals(OdrlOperator.GEQ)) {
            endDate = false;
        } else {
            log.error("Failed to parse operator in timed policy {}", constraint);
            return null;  // unknown type of time policy
        }

        // try to parse as time agreement offset policy
        TimeAgreementOffsetPolicy policy = parseTimeAgreementOffsetPolicy(constraint, endDate);
        if (policy != null) {
            return policy;
        }

        // try to parse as time date policy
        return parseTimeDatePolicy(constraint, endDate);
    }

    private TimeAgreementOffsetPolicy parseTimeAgreementOffsetPolicy(OdrlConstraint constraint, boolean endDate) {

        var matcher = Pattern.compile("(contract[A,a]greement)\\+(-?[0-9]+)(s|m|h|d)")
            .matcher(constraint.getRightOperand());
        if (matcher.matches()) {
            int number = Integer.parseInt(matcher.group(2));
            AgreementOffsetUnit unit = AgreementOffsetUnit.forValue(matcher.group(3));
            return endDate
                ? EndAgreementOffsetPolicy.builder().offsetNumber(number).offsetUnit(unit).build()
                : StartAgreementOffsetPolicy.builder().offsetNumber(number).offsetUnit(unit).build();
        }
        return null;
    }

    private TimeDatePolicy parseTimeDatePolicy(OdrlConstraint constraint, boolean endDate) {

        try {
            OffsetDateTime date = OffsetDateTime.parse(constraint.getRightOperand());
            return endDate ? EndDatePolicy.builder().date(date).build() : StartDatePolicy.builder().date(date).build();
        } catch (Exception e) {
            log.error("Failed to parse timestamp in policy {}", constraint, e);
            return null;
        }
    }

    /**
     * Given a list of edc policies, compute their validity based on the contract signing date and the provider DID.
     *
     * @param edcPolicies list of edc policies
     * @param contractSigningDate contract signing date
     * @param providerDid provider DID
     * @return list of enforcement policies with validity
     */
    @Override
    public List<EnforcementPolicy> getEnforcementPoliciesWithValidity(List<Policy> edcPolicies,
        BigInteger contractSigningDate, String providerDid) {

        List<EnforcementPolicy> enforcementPolicies = getEnforcementPoliciesFromEdcPolicies(edcPolicies);
        computePolicyValidities(enforcementPolicies, contractSigningDate == null
                ? null
                : OffsetDateTime.ofInstant(Instant.ofEpochMilli(contractSigningDate.longValue()), ZoneId.systemDefault()),
            providerDid);
        return enforcementPolicies;
    }

    private void computePolicyValidities(List<EnforcementPolicy> policies, OffsetDateTime contractAgreementTime,
        String providerDid) {

        for (EnforcementPolicy policy : policies) {
            boolean isValid = false;
            OffsetDateTime now = OffsetDateTime.now();
            if (policy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) {
                isValid = providerDid.equals(participantId) || participantRestrictionPolicy.getAllowedParticipants()
                    .contains(participantId);
            } else if (policy instanceof StartDatePolicy startDatePolicy) {
                isValid = startDatePolicy.getDate().isBefore(now);
            } else if (policy instanceof EndDatePolicy endDatePolicy) {
                isValid = endDatePolicy.getDate().isAfter(now);
            } else if (policy instanceof StartAgreementOffsetPolicy startAgreementOffsetPolicy) {
                Duration offset = getOffsetFromTimeAgreementOffsetPolicy(startAgreementOffsetPolicy);
                isValid = contractAgreementTime != null && contractAgreementTime.plus(offset).isBefore(now);
            } else if (policy instanceof EndAgreementOffsetPolicy endAgreementOffsetPolicy) {
                Duration offset = getOffsetFromTimeAgreementOffsetPolicy(endAgreementOffsetPolicy);
                isValid = contractAgreementTime != null && contractAgreementTime.plus(offset).isAfter(now);
            } else if (policy instanceof EverythingAllowedPolicy) {
                isValid = true;
            } else {
                log.error("Could not compute validity for unknown policy type: {}", policy.getClass().getName());
            }
            policy.setValid(isValid);
        }
    }

    private Duration getOffsetFromTimeAgreementOffsetPolicy(TimeAgreementOffsetPolicy policy) {

        return switch (policy.getOffsetUnit()) {
            case DAYS -> Duration.ofDays(policy.getOffsetNumber());
            case HOURS -> Duration.ofHours(policy.getOffsetNumber());
            case MINUTES -> Duration.ofMinutes(policy.getOffsetNumber());
            case SECONDS -> Duration.ofSeconds(policy.getOffsetNumber());
        };
    }

    /**
     * Given a list of enforcement policies, convert them to a single policy that can be given to the EDC for
     * evaluation.
     *
     * @param enforcementPolicies list of enforcement policies and their constraints
     * @return edc policy
     */
    @Override
    public Policy getEdcPolicyFromEnforcementPolicies(List<EnforcementPolicy> enforcementPolicies) {

        List<OdrlConstraint> constraints = new ArrayList<>();

        // iterate over all enforcement policies and add a constraint per entry
        for (EnforcementPolicy enforcementPolicy : enforcementPolicies) {
            if (enforcementPolicy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) { // restrict to participants

                // create constraint
                OdrlConstraint participantConstraint = OdrlConstraint.builder()
                    .leftOperand(ParticipantRestrictionPolicy.EDC_OPERAND).operator(OdrlOperator.IN)
                    .rightOperand(String.join(",", participantRestrictionPolicy.getAllowedParticipants())).build();
                constraints.add(participantConstraint);
            } else if (enforcementPolicy instanceof TimeDatePolicy timeDatePolicy) { // restrict to fixed time

                boolean isEndDate = timeDatePolicy instanceof EndDatePolicy;
                // create constraint
                OdrlConstraint timeConstraint = OdrlConstraint.builder().leftOperand(TimeDatePolicy.EDC_OPERAND)
                    .operator(isEndDate ? OdrlOperator.LEQ : OdrlOperator.GEQ)
                    .rightOperand(DateTimeFormatter.ISO_DATE_TIME.format(timeDatePolicy.getDate()))
                    .build(); // ISO 8601 date
                constraints.add(timeConstraint);
            } else if (enforcementPolicy instanceof TimeAgreementOffsetPolicy timeAgreementOffsetPolicy) { // restrict to time after agreement

                boolean isEndOffset = timeAgreementOffsetPolicy instanceof EndAgreementOffsetPolicy;
                // create constraint
                OdrlConstraint timeConstraint = OdrlConstraint.builder()
                    .leftOperand(TimeAgreementOffsetPolicy.EDC_OPERAND)
                    .operator(isEndOffset ? OdrlOperator.LEQ : OdrlOperator.GEQ).rightOperand(
                        "contractAgreement+" + timeAgreementOffsetPolicy.getOffsetNumber()
                            + timeAgreementOffsetPolicy.getOffsetUnit().toValue())
                    .build(); // format "contractAgreement+<number><unit>"
                constraints.add(timeConstraint);
            } // else unknown or everything allowed => no constraint
        }

        // apply constraints to both use and transfer permission
        Policy policy = getEverythingAllowedPolicy();

        policy.getPermission().forEach(permission -> permission.setConstraint(constraints));

        return policy;
    }

    /**
     * Get EDC base policy that can be extended with constraints.
     *
     * @return everything allowed policy
     */
    @Override
    public Policy getEverythingAllowedPolicy() {

        OdrlPermission usePermission = OdrlPermission.builder().action(OdrlAction.USE).build();
        OdrlPermission transferPermission = OdrlPermission.builder().action(OdrlAction.TRANSFER).build();

        // add permissions to ODRL policy
        Policy policy = new Policy();
        policy.getPermission().add(usePermission);
        policy.getPermission().add(transferPermission);
        return policy;
    }
}
