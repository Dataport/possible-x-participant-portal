package eu.possiblex.participantportal.application.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ServiceOfferingShapeRestApi {
    @Operation(summary = "Get the Gaia-X service offering shape", tags = {
        "Shapes" }, description = "Get the Gaia-X service offering shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X service offering shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/serviceoffering")
    String getGxServiceOfferingShape();
}
