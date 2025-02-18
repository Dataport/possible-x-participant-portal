package eu.possiblex.participantportal.application.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ResourceShapeRestApi {
    @Operation(summary = "Get the Gaia-X data resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X data resource shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X data resource shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/resource/dataresource")
    String getGxDataResourceShape();

    @Operation(summary = "Get the Gaia-X instantiated virtual resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X instantiated virtual resource shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X instantiated virtual resource shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/resource/instantiatedvirtualresource")
    String getGxInstantiatedVirtualResourceShape();

    @Operation(summary = "Get the Gaia-X physical resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X physical resource shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X physical resource shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/resource/physicalresource")
    String getGxPhysicalResourceShape();

    @Operation(summary = "Get the Gaia-X software resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X software resource shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X software resource shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/resource/softwareresource")
    String getGxSoftwareResourceShape();

    @Operation(summary = "Get the Gaia-X virtual resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X virtual resource shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X virtual resource shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/resource/virtualresource")
    String getGxVirtualResourceShape();

    @Operation(summary = "Get the Gaia-X legitimate interest shape", tags = {
        "Shapes" }, description = "Get the Gaia-X legitimate interest shape.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "Gaia-X legitimate interest shape as JSON string", example = "{ \"prefixList\": [], \"shapes\": [] }"))) })
    @GetMapping("/gx/resource/legitimateinterest")
    String getGxLegitimateInterestShape();
}
