package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.ParticipantDetailsTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mapper(componentModel = "spring")
public interface ParticipantApiMapper {

    @Mapping(target = "participantId", source = "id")
    ParticipantIdTO idToParticipantIdTO(String id);

    @Mapping(target = "participantId", source = "id", qualifiedByName = "extractParticipantDidWeb")
    @Mapping(target = "participantName", source = "name")
    @Mapping(target = "participantEmail", source = "mailAddress")
    ParticipantDetailsTO credentialSubjectSubsetToParticipantDetailsTO(
        PxExtendedLegalParticipantCredentialSubjectSubset participant);

    @Named("extractParticipantDidWeb")
    default String extractParticipantDid(String id) {

        String didWebRegex = "did:web.*";

        Pattern pattern = Pattern.compile(didWebRegex);
        Matcher matcher = pattern.matcher(id);

        String extracted = "";
        if (matcher.find()) {
            extracted = matcher.group();
        }
        return extracted;
    }
}
