package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/contract")
public interface ContractRestApi {
    /**
     * GET request for retrieving all contract agreements.
     *
     * @return list of contract agreements
     */
    @GetMapping(value = "/agreement", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ContractAgreementTO> getContractAgreements();

    /**
     * GET request for retrieving contract details by contract agreement id.
     *
     * @return contract details
     */
    @GetMapping(value = "/details/{contractAgreementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ContractDetailsTO getContractDetailsByContractAgreementId(@PathVariable String contractAgreementId);

    /**
     * GET request for retrieving the referenced offer with timestamp by contract agreement id.
     *
     * @return Referenced offer with timestamp
     */
    @GetMapping(value = "/details/{contractAgreementId}/offer", produces = MediaType.APPLICATION_JSON_VALUE)
    OfferWithTimestampTO getOfferWithTimestampByContractAgreementId(@PathVariable String contractAgreementId);

    /**
     * POST request for transferring a data product again from the contracts tab.
     *
     * @param request the request containing the assetID, the contract agreement id and the provider url
     * @return the response containing the status of the transfer process
     */
    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferOfferResponseTO transferDataOfferAgain(@RequestBody TransferOfferRequestTO request);
}
