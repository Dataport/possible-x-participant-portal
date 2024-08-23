package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBO;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBO;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;

public interface ConsumerService {
    DcatDataset selectContractOffer(SelectOfferRequestBO request);

    TransferProcess acceptContractOffer(ConsumeOfferRequestBO request);
}
