package eu.possiblex.participantportal.application.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.selfdescriptions.PojoCredentialSubject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CredentialSubjectUtils { //TODO adapt class when JsonNode has been replaced
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param type type class to search for
     * @param <T> type of the class to search for must extend pojo credential subjects and have a TYPE attribute
     * @return list of pojo credential subjects
     */
    public static <T extends PojoCredentialSubject> List<T> findAllCredentialSubjectsByType(Class<T> type,
        List<JsonNode> csList) {

        String typeString;

        try {
            typeString = (String) type.getField("TYPE").get(null);

        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return Collections.emptyList();
        }

        return csList.stream().filter(Objects::nonNull)
            .filter(cs -> cs.has("type") && cs.get("type").textValue().equals(typeString)).map(cs -> {
                try {
                    return objectMapper.treeToValue(cs, type);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to cast JsonNode to " + type.getName(), e);
                }
            }).collect(Collectors.toList());
    }

    /**
     * @param type type class to search for
     * @param <T> type of the class to search for must extend pojo credential subjects and have a TYPE attribute
     * @return credential subject as pojo or null if it does not exist within the VP
     */
    public static <T extends PojoCredentialSubject> T findFirstCredentialSubjectByType(Class<T> type,
        List<JsonNode> csList) {

        List<T> pojoCredentialSubjects = findAllCredentialSubjectsByType(type, csList);
        if (pojoCredentialSubjects.isEmpty()) {
            return null;
        }
        return pojoCredentialSubjects.get(0);
    }
}
