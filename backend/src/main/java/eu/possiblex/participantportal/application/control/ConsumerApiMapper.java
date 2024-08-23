package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOtoBO(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOtoBO(ConsumeOfferRequestTO to);

    TransferDetailsTO transferProcessToDetailsTO(TransferProcess process);
}
