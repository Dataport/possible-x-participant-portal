/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
