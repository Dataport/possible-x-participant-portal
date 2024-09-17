package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.utilities.PossibleXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for managing provider-related operations.
 */
@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ProviderRestApiImpl implements ProviderRestApi {

    private final ProviderService providerService;
    private final ProviderApiMapper providerApiMapper;

    /**
     * Constructor for ProviderRestApiImpl.
     *
     * @param providerService the provider service
     * @param providerApiMapper the provider API mapper
     */
    @Autowired
    public ProviderRestApiImpl(ProviderService providerService, ProviderApiMapper providerApiMapper) {
        this.providerService = providerService;
        this.providerApiMapper = providerApiMapper;
    }

    /**
     * POST endpoint to create an offer.
     *
     * @param createOfferRequestTO the create offer request transfer object
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO) throws PossibleXException{
        CreateFhOfferBE createFhOfferBE = providerApiMapper.getCreateDatasetEntryDTOFromCreateOfferRequestTO(createOfferRequestTO);
        CreateEdcOfferBE createEdcOfferBE = providerApiMapper.getCreateEdcOfferDTOFromCreateOfferRequestTO(createOfferRequestTO);

        CreateOfferResponseTO response = providerService.createOffer(createFhOfferBE, createEdcOfferBE);
        if (response == null) {
            throw new PossibleXException("Couldn't create offer", HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}