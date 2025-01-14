package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

        log.info("selecting contract with {}", request);
        SelectOfferRequestBE be = consumerApiMapper.selectOfferRequestTOToBE(request);
        SelectOfferResponseBE response;
        try {
            response = consumerService.selectContractOffer(be);
        } catch (OfferNotFoundException e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getFhCatalogOfferId() + ". OfferNotFoundException: "
                    + e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getFhCatalogOfferId() + ". Other Exception: " + e);
        }

        log.info("returning for selecting contract: {}", response);
        return consumerApiMapper.selectOfferResponseBEToOfferDetailsTO(response);
    }

    @Override
    public AcceptOfferResponseTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        log.info("accepting contract with {}", request);
        ConsumeOfferRequestBE be = consumerApiMapper.consumeOfferRequestTOToBE(request);

        AcceptOfferResponseBE acceptOffer;
        try {
            acceptOffer = consumerService.acceptContractOffer(be);
        } catch (OfferNotFoundException e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getEdcOfferId() + ". OfferNotFoundException: " + e,
                HttpStatus.NOT_FOUND);
        } catch (NegotiationFailedException e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getEdcOfferId() + ". NegotiationFailedException: " + e);
        } catch (Exception e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getEdcOfferId() + ". Other Exception: " + e);
        }

        if (acceptOffer.isDataOffering()) {
            log.info("DataResource found: Transfer has been initiated");
        } else {
            log.info("No dataResource found: Transfer is omitted");
        }
        AcceptOfferResponseTO response = consumerApiMapper.acceptOfferResponseBEToAcceptOfferResponseTO(acceptOffer);
        log.info("Returning for accepting contract: {}", response);
        return response;
    }

    @Override
    public TransferOfferResponseTO transferDataOffer(@RequestBody TransferOfferRequestTO request) {

        log.info("transferring data from contract with {}", request);
        TransferOfferRequestBE be = consumerApiMapper.transferOfferRequestTOToBE(request);

        TransferOfferResponseBE transferOfferResponseBE;
        try {
            transferOfferResponseBE = consumerService.transferDataOffer(be);
        } catch (OfferNotFoundException e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getEdcOfferId() + ". OfferNotFoundException: " + e,
                HttpStatus.NOT_FOUND);
        } catch (TransferFailedException e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getEdcOfferId() + ". TransferFailedException: " + e);
        } catch (Exception e) {
            throw new PossibleXException(
                "Failed to select offer with offerId" + request.getEdcOfferId() + ". Other Exception: " + e);
        }

        TransferOfferResponseTO response = consumerApiMapper.transferOfferResponseBEToTransferOfferResponseTO(
            transferOfferResponseBE);
        log.info("Returning for transferring data of contract: {}", response);
        return response;
    }
}