package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.OfferDetailsTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferResponseBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
@Slf4j
public class ConsumerRestApiImpl implements ConsumerRestApi {

    private final ConsumerService consumerService;

    private final ConsumerApiMapper consumerApiMapper;

    public ConsumerRestApiImpl(@Autowired ConsumerService consumerService,
        @Autowired ConsumerApiMapper consumerApiMapper) {

        this.consumerService = consumerService;
        this.consumerApiMapper = consumerApiMapper;
    }

    @Override
    public OfferDetailsTO selectContractOffer(@RequestBody SelectOfferRequestTO request) {

        log.info("selecting contract with " + request);
        SelectOfferRequestBE be = consumerApiMapper.selectOfferRequestTOtoBE(request);
        SelectOfferResponseBE response;
        try {
            response = consumerService.selectContractOffer(be);
        } catch (OfferNotFoundException e) {
            throw new PossibleXException("Failed to select offer with offerId" + request.getFhCatalogOfferId() + ". OfferNotFoundException: " + e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new PossibleXException("Failed to select offer with offerId" + request.getFhCatalogOfferId() + ". Other Exception: " + e);
        }

        log.info("returning for selecting contract: " + response);
        return consumerApiMapper.selectOfferResponseBEToOfferDetailsTO(response);
    }

    @Override
    public TransferDetailsTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        log.info("accepting contract with " + request);
        ConsumeOfferRequestBE be = consumerApiMapper.consumeOfferRequestTOtoBE(request);

        TransferProcess process;
        try {
            process = consumerService.acceptContractOffer(be);
        } catch (OfferNotFoundException e) {
            throw new PossibleXException("Failed to select offer with offerId" + request.getEdcOfferId() + ". OfferNotFoundException: " + e, HttpStatus.NOT_FOUND);
        } catch (NegotiationFailedException e) {
            throw new PossibleXException("Failed to select offer with offerId" + request.getEdcOfferId() + ". NegotiationFailedException: " + e);
        } catch (TransferFailedException e) {
            throw new PossibleXException("Failed to select offer with offerId" + request.getEdcOfferId() + ". TransferFailedException: " + e);
        } catch (Exception e) {
            throw new PossibleXException("Failed to select offer with offerId" + request.getEdcOfferId() + ". Other Exception: " + e);
        }

        TransferDetailsTO response = consumerApiMapper.transferProcessToDetailsTO(process);

        log.info("returning for accepting contract: " + response);
        return  response;
    }
}