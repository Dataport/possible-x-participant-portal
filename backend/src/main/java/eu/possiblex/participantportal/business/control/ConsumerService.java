package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;

public interface ConsumerService {
    DcatDataset selectContractOffer(SelectOfferRequestBE request);

    TransferProcess acceptContractOffer(ConsumeOfferRequestBE request);
}
