package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ProviderRestApiImpl implements ProviderRestApi {

    private final ProviderService providerService;

    private final ProviderApiMapper providerApiMapper;

    public ProviderRestApiImpl(@Autowired ProviderService providerService,
        @Autowired ProviderApiMapper providerApiMapper) {

        this.providerService = providerService;
        this.providerApiMapper = providerApiMapper;
    }

    /**
     * POST endpoint to create an offer
     *
     * @return success message
     */
    @Override
    public CreateOfferResponseTO createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO) {

        CreateFhOfferBE createFhOfferBE = providerApiMapper.getCreateDatasetEntryDTOFromCreateOfferRequestTO(
            createOfferRequestTO);
        CreateEdcOfferBE createEdcOfferBE = providerApiMapper.getCreateEdcOfferDTOFromCreateOfferRequestTO(
            createOfferRequestTO);

        CreateOfferResponseTO createOfferResponseTO = null;
        try {
            createOfferResponseTO = providerService.createOffer(createFhOfferBE, createEdcOfferBE);
        } catch (EdcOfferCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "EDC offer creation failed: " + e.getMessage());
        } catch (FhOfferCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Fraunhofer catalog offer creation failed: " + e.getMessage());
        }

        return createOfferResponseTO;
    }

}
