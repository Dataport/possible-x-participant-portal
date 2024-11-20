package eu.possiblex.participantportal.application.boundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ResourceShapeRestApi {
    /**
     * GET request for retrieving the Gaia-X data resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/dataresource")
    public String getGxDataResourceShape();

    /**
     * GET request for retrieving the Gaia-X instantiated virtual resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/instantiatedvirtualresource")
    public String getGxInstantiatedVirtualResourceShape();

    /**
     * GET request for retrieving the Gaia-X physical resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/physicalresource")
    public String getGxPhysicalResourceShape();

    /**
     * GET request for retrieving the Gaia-X software resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/softwareresource")
    public String getGxSoftwareResourceShape();

    /**
     * GET request for retrieving the Gaia-X virtual resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/virtualresource")
    public String getGxVirtualResourceShape();

    /**
     * GET request for retrieving the Gaia-X legitimate interest shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/legitimateinterest")
    public String getGxLegitimateInterestShape();
}
