package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * @param createServiceOfferingRequestTO the create offering request transfer object
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createServiceOffering(
        @RequestBody CreateServiceOfferingRequestTO createServiceOfferingRequestTO) {

        log.info("CreateServiceOfferingRequestTO: {}", createServiceOfferingRequestTO);

        CreateServiceOfferingRequestBE createOfferingRequestBE = providerApiMapper.getCreateOfferingRequestBE(
            createServiceOfferingRequestTO);

        return providerService.createOffering(createOfferingRequestBE);
    }

    /**
     * POST endpoint to create a data offering
     *
     * @param createDataOfferingRequestTO the create offering request transfer object
     * @return create offer response object
     */
    @Override
    public CreateOfferResponseTO createDataOffering(
        @RequestBody CreateDataOfferingRequestTO createDataOfferingRequestTO) {

        log.info("CreateDataOfferingRequestTO: {}", createDataOfferingRequestTO);

        CreateDataOfferingRequestBE createOfferingRequestBE = providerApiMapper.getCreateOfferingRequestBE(
            createDataOfferingRequestTO);

        return providerService.createOffering(createOfferingRequestBE);
    }
}