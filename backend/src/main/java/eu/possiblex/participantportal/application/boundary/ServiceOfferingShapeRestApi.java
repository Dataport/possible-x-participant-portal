package eu.possiblex.participantportal.application.boundary;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ServiceOfferingShapeRestApi {
    @Operation(summary = "Get the Gaia-X service offering shape", tags = {
        "Shapes" }, description = "Get the Gaia-X service offering shape.")
    @GetMapping("/gx/serviceoffering")
    String getGxServiceOfferingShape();
}
