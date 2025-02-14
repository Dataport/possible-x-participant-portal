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
