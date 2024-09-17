package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.business.entity.selfdescriptions.PojoCredentialSubject;

import java.util.List;
import java.util.Objects;

public class CredentialSubjectUtils {


    /**
     * @param type type class to search for
     * @param <T> type of the class to search for must extend pojo credential subjects and have a TYPE attribute
     * @return list of pojo credential subjects
     */
    public static <T extends PojoCredentialSubject> List<T> findAllCredentialSubjectsByType(Class<T> type,
        List<PojoCredentialSubject> csList) {

        return csList.stream().filter(Objects::nonNull)
            .filter(type::isInstance)
            .map(cs -> (T) cs)
            .toList();
    }

    /**
     * @param type type class to search for
     * @param <T> type of the class to search for must extend pojo credential subjects and have a TYPE attribute
     * @return credential subject as pojo or null if it does not exist within the VP
     */
    public static <T extends PojoCredentialSubject> T findFirstCredentialSubjectByType(Class<T> type,
        List<PojoCredentialSubject> csList) {

        List<T> pojoCredentialSubjects = findAllCredentialSubjectsByType(type, csList);
        if (pojoCredentialSubjects.isEmpty()) {
            return null;
        }
        return pojoCredentialSubjects.get(0);
    }
}
