package eu.possiblex.participantportal.application.boundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ServiceOfferingShapeRestApi {
    /**
     * GET request for retrieving the Gaia-X service offering shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/serviceoffering")
    String getGxServiceOfferingShape();
}
