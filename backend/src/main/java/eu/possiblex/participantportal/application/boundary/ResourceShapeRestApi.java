package eu.possiblex.participantportal.application.boundary;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ResourceShapeRestApi {
    @Operation(summary = "Get the Gaia-X data resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X data resource shape.")
    @GetMapping("/gx/resource/dataresource")
    String getGxDataResourceShape();

    @Operation(summary = "Get the Gaia-X instantiated virtual resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X instantiated virtual resource shape.")
    @GetMapping("/gx/resource/instantiatedvirtualresource")
    String getGxInstantiatedVirtualResourceShape();

    @Operation(summary = "Get the Gaia-X physical resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X physical resource shape.")
    @GetMapping("/gx/resource/physicalresource")
    String getGxPhysicalResourceShape();

    @Operation(summary = "Get the Gaia-X software resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X software resource shape.")
    @GetMapping("/gx/resource/softwareresource")
    String getGxSoftwareResourceShape();

    @Operation(summary = "Get the Gaia-X virtual resource shape", tags = {
        "Shapes" }, description = "Get the Gaia-X virtual resource shape.")
    @GetMapping("/gx/resource/virtualresource")
    String getGxVirtualResourceShape();

    @Operation(summary = "Get the Gaia-X legitimate interest shape", tags = {
        "Shapes" }, description = "Get the Gaia-X legitimate interest shape.")
    @GetMapping("/gx/resource/legitimateinterest")
    String getGxLegitimateInterestShape();
}
