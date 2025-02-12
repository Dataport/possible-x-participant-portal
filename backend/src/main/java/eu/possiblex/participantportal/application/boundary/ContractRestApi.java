package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/contract")
public interface ContractRestApi {
    @Operation(summary = "Get all contract agreements", tags = {
        "Contract" }, description = "Get all contract agreements.")
    @GetMapping(value = "/agreement", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ContractAgreementTO> getContractAgreements();

    @Operation(summary = "Get the details of a specific contract", tags = {
        "Contract" }, description = "Get the details of the contract with the given contract agreement ID.", parameters = {
        @Parameter(name = "contractAgreementId", description = "The contract agreement ID for which to get the related offer.", example = "a49549f6-fb07-4b9c-86bf-1d8af45b8068") })
    @GetMapping(value = "/details/{contractAgreementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ContractDetailsTO getContractDetailsByContractAgreementId(@PathVariable String contractAgreementId);

    @Operation(summary = "Get a specific offering with retrieval timestamp", tags = {
        "Contract" }, description = "Get the related offering including retrieval timestamp for the given contract agreement ID.", parameters = {
        @Parameter(name = "contractAgreementId", description = "The contract agreement ID for which to get the related offer.", example = "a49549f6-fb07-4b9c-86bf-1d8af45b8068") })
    @GetMapping(value = "/details/{contractAgreementId}/offer", produces = MediaType.APPLICATION_JSON_VALUE)
    OfferWithTimestampTO getOfferWithTimestampByContractAgreementId(@PathVariable String contractAgreementId);

    @Operation(summary = "Initiate a data transfer", tags = {
        "Contract" }, description = "Initiate a data transfer for an offer with the given counter party, contract agreement and EDC offer ID.")
    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferOfferResponseTO transferDataOfferAgain(@RequestBody TransferOfferRequestTO request);
}
