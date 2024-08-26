package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.possiblex.participantportal.application.control.RequestMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.fh.CreateDatasetEntryBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ProviderRestApi {

    private final ProviderService providerService;

    private final ObjectMapper objectMapper;

    private final RequestMapper requestMapper;

    public ProviderRestApi(@Autowired ProviderService providerService, @Autowired ObjectMapper objectMapper,
        @Autowired RequestMapper requestMapper) {

        this.providerService = providerService;
        this.objectMapper = objectMapper;
        this.requestMapper = requestMapper;
    }

    /**
     * POST endpoint to create an offer
     *
     * @return success message
     */
    @PostMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonNode createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO) {

        CreateDatasetEntryBE createDatasetEntryBE = requestMapper.getCreateDatasetEntryDTOFromCreateOfferRequestTO(
            createOfferRequestTO);
        CreateEdcOfferBE createEdcOfferBE = requestMapper.getCreateEdcOfferDTOFromCreateOfferRequestTO(
            createOfferRequestTO);

        IdResponse response = providerService.createOffer(createDatasetEntryBE, createEdcOfferBE);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", response.getId());
        return node;
    }

}
