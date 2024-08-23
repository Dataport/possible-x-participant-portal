package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBO;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBO;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerApiMapper {

    SelectOfferRequestBO selectOfferRequestTOtoBO(SelectOfferRequestTO to);

    ConsumeOfferRequestBO consumeOfferRequestTOtoBO(ConsumeOfferRequestTO to);

    TransferDetailsTO transferProcessToDetailsTO(TransferProcess process);
}
