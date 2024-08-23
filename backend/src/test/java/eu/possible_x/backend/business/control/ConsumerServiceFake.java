package eu.possible_x.backend.business.control;

import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.IonosS3TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;

public class ConsumerServiceFake implements ConsumerService {
    @Override
    public DcatDataset selectContractOffer(SelectOfferRequestBE request) {

        return new DcatDataset();
    }

    @Override
    public TransferProcess acceptContractOffer(ConsumeOfferRequestBE request) {

        return new IonosS3TransferProcess();
    }
}
