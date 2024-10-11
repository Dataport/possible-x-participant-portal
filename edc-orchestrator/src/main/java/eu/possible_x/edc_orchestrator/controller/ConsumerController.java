package eu.possible_x.edc_orchestrator.controller;


import eu.possible_x.edc_orchestrator.entities.ConsumeOfferRequest;
import eu.possible_x.edc_orchestrator.entities.edc.asset.ionoss3extension.IonosS3DataAddress;
import eu.possible_x.edc_orchestrator.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ConsumerController {

  private final ConsumerService consumerService;

  public ConsumerController(@Autowired ConsumerService consumerService) {
    this.consumerService = consumerService;
  }

  /**
   * POST endpoint to accept a Contract Offer
   *
   * @return Data Address of the transferred data
   */
  @PostMapping(value = "/acceptContractOffer", produces = MediaType.APPLICATION_JSON_VALUE)
  public String acceptContractOffer(@RequestBody ConsumeOfferRequest request) {
    IonosS3DataAddress dataAddress = consumerService.acceptContractOffer(request);
    return "{\"dataAddress\": \"" + dataAddress + "\"}";
  }
}
