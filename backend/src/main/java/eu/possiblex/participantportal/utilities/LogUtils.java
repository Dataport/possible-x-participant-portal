package eu.possiblex.participantportal.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/*
 * Some util functions for logging.
 */
public class LogUtils {

    /**
     * Serialize an object to JSON.
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
