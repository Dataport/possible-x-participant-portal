package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.OfferDetailsTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOtoBO(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOtoBO(ConsumeOfferRequestTO to);

    @Mapping(source = "assetId", target = "offerId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "contenttype", target = "contentType")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "hasPolicy", target = "policies")
    OfferDetailsTO dcatDatasetToOfferDetailsTo(DcatDataset dataset);

    TransferDetailsTO transferProcessToDetailsTO(TransferProcess process);
}
