package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ParticipantNameBE;
import eu.possiblex.participantportal.business.entity.ParticipantWithMailBE;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ConsumerServiceMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "mailAddress", source = "mailAddress")
    @Mapping(target = "did", source = "uri")
    ParticipantWithMailBE mapToParticipantWithMailBE(
        ParticipantDetailsSparqlQueryResult participantDetailsSparqlQueryResult);
}
