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

package eu.possiblex.participantportal.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
/*
 * Some util functions for logging.
 */ public class LogUtils {

    private LogUtils() {
        // Utility class
    }

    /**
     * Creates a ClientHttpConnector for logging (outgoing) REST calls.
     *
     * @return the logging ClientHttpConnector
     */
    public static ClientHttpConnector createHttpClient() {

        HttpClient httpClient = HttpClient.create()
            .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        return new ReactorClientHttpConnector(httpClient);
    }

    /**
     * Serialize an object to JSON.
     *
     * @param o the object to serialize
     * @return the JSON
     */
    public static String serializeObjectToJson(Object o) {

        ObjectMapper om = new ObjectMapper();

        try {
            return om.writeValueAsString(o);
        } catch (Exception e) {
            log.error("could not serialize object", e);
            return "could not serialize object";
        }
    }
}
