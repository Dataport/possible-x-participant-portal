package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.OfferDetailsTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferResponseBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOtoBE(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOtoBE(ConsumeOfferRequestTO to);

    @Mapping(target = "edcOfferId", source = "edcOffer.assetId")
    @Mapping(target = "offerType", constant = "Data Resource") // TODO pass actual data
    @Mapping(target = "creationDate", expression = "java(OffsetDateTime.now())") // TODO pass actual data
    @Mapping(target = "name", source = "edcOffer.name")
    @Mapping(target = "description", source = "edcOffer.description")
    @Mapping(target = "contentType", source = "edcOffer.contenttype")
    @Mapping(target = "counterPartyAddress", source = "counterPartyAddress")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    TransferDetailsTO transferProcessToDetailsTO(TransferProcess process);
}
