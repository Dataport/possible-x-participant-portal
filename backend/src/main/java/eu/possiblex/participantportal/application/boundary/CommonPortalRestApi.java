package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.VersionTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/common")
public interface CommonPortalRestApi {
    @Operation(summary = "Get the version of the participant portal", tags = {
        "Common" }, description = "Get the version of the participant portal.")
    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    VersionTO getVersion();

    @Operation(summary = "Get the ID to name mapping for all participants", tags = {
        "Common" }, description = "Get the ID to name mapping for all participants that are published in the catalogue.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(description = "A map of participant IDs to their names", example = "{\"did:web:example.com:participant:someorgltd\": \"Some Organization Ltd.\", \"did:web:example.com:participant:otherorgltd\": \"Other Organization Ltd.\"}"))) })
    @GetMapping(value = "/participant/name-mapping", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> getNameMapping();
}
