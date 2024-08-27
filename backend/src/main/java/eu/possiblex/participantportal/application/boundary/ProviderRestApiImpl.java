package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.fh.CreateDatasetEntryBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ProviderRestApiImpl implements ProviderRestApi {

    private final ProviderService providerService;

    private final ObjectMapper objectMapper;

    private final ProviderApiMapper providerApiMapper;

    public ProviderRestApiImpl(@Autowired ProviderService providerService, @Autowired ObjectMapper objectMapper,
        @Autowired ProviderApiMapper providerApiMapper) {

        this.providerService = providerService;
        this.objectMapper = objectMapper;
        this.providerApiMapper = providerApiMapper;
    }

    @Override
    public JsonNode createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO) {

        CreateDatasetEntryBE createDatasetEntryBE = providerApiMapper.getCreateDatasetEntryDTOFromCreateOfferRequestTO(
            createOfferRequestTO);
        CreateEdcOfferBE createEdcOfferBE = providerApiMapper.getCreateEdcOfferDTOFromCreateOfferRequestTO(
            createOfferRequestTO);

        IdResponse response = providerService.createOffer(createDatasetEntryBE, createEdcOfferBE);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", response.getId());
        return node;
    }

}
