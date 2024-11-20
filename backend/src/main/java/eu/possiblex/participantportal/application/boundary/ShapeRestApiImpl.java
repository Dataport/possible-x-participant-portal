package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.SdCreationWizardApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class ShapeRestApiImpl implements ServiceOfferingShapeRestApi, ResourceShapeRestApi {

    private static final String ECOSYSTEM_GAIAX = "gx";

    private final SdCreationWizardApiService sdCreationWizardApiService;

    public ShapeRestApiImpl(@Autowired SdCreationWizardApiService sdCreationWizardApiService) {

        this.sdCreationWizardApiService = sdCreationWizardApiService;
    }

    /**
     * GET request for retrieving the Gaia-X data resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxDataResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Dataresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X instantiated virtual resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxInstantiatedVirtualResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Instantiatedvirtualresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X physical resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxPhysicalResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Physicalresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X software resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxSoftwareResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Softwareresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X virtual resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxVirtualResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Virtualresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X legitimate interest shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxLegitimateInterestShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Legitimateinterest.json");
    }

    /**
     * GET request for retrieving the Gaia-X service offering shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxServiceOfferingShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Serviceoffering.json");
    }
}
