package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public CreateOfferResponseTO createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO) {

        log.info("CreateOfferRequestTO: {}", createOfferRequestTO);

        CreateFhOfferBE createFhOfferBE = providerApiMapper.getCreateDatasetEntryDTOFromCreateOfferRequestTO(
            createOfferRequestTO);
        CreateEdcOfferBE createEdcOfferBE = providerApiMapper.getCreateEdcOfferDTOFromCreateOfferRequestTO(
            createOfferRequestTO);

        try {
            return providerService.createOffer(createFhOfferBE, createEdcOfferBE);
        } catch (EdcOfferCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "EDC offer creation failed: " + e.getMessage());
        } catch (FhOfferCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Fraunhofer catalog offer creation failed: " + e.getMessage());
        }
    }

    /**
     * GET endpoint to retrieve the participant's id
     *
     * @return participant id
     */
    @Override
    public ParticipantIdTO getParticipantId() {

        return providerService.getParticipantId();
    }
}