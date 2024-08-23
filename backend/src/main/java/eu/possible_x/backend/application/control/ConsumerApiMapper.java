package eu.possible_x.backend.application.control;

import eu.possible_x.backend.application.entity.ConsumeOfferRequestTO;
import eu.possible_x.backend.application.entity.SelectOfferRequestTO;
import eu.possible_x.backend.application.entity.TransferDetailsTO;
import eu.possible_x.backend.business.entity.ConsumeOfferRequestBE;
import eu.possible_x.backend.business.entity.SelectOfferRequestBE;
import eu.possible_x.backend.business.entity.edc.transfer.TransferProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOtoBE(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOtoBE(ConsumeOfferRequestTO to);

    TransferDetailsTO transferProcessToDetailsTO(TransferProcess process);
}
