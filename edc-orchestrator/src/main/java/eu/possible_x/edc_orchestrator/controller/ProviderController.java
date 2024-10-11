package eu.possible_x.edc_orchestrator.controller;

import eu.possible_x.edc_orchestrator.entities.AssetRequest;
import eu.possible_x.edc_orchestrator.entities.edc.common.IdResponse;
import eu.possible_x.edc_orchestrator.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ProviderController {

  private final ProviderService providerService;

  public ProviderController(@Autowired ProviderService providerService) {

      this.providerService = providerService;
  }

  /**
   * POST endpoint to create an asset
   *
   * @param assetRequest: request to create an asset
   * @return success message
   */
  @PostMapping(value = "/asset", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createAsset(@RequestBody AssetRequest assetRequest) {
   return ResponseEntity.ok("Success: create the asset with name: " + assetRequest.getAssetName());
  }

  /**
   * POST endpoint to create an offer
   *
   * @return success message
   */
  @PostMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
  public String createOffer() {
    IdResponse response = providerService.createOffer();
    return "{\"id\": \"" + response.getId() + "\"}";
  }

}
