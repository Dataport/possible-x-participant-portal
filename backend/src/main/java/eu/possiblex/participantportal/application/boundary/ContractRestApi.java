package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.ContractAgreementTO;
import eu.possiblex.participantportal.application.entity.TransferOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferOfferResponseTO;
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
     * GET request for retrieving a contract agreement by id.
     *
     * @return contract agreement
     */
    @GetMapping(value = "/agreement/{agreementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ContractAgreementTO getContractAgreementById(@PathVariable String agreementId);

    /**
     * POST request for transferring a data product again from the contracts tab.
     *
     * @param request the request containing the assetID, the contract agreement id and the provider url
     * @return the response containing the status of the transfer process
     */
    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferOfferResponseTO transferDataOfferAgain(@RequestBody TransferOfferRequestTO request);
}
