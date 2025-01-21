package eu.possiblex.participantportal.business.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommonPortalServiceImpl implements CommonPortalService {

    private final FhCatalogClient fhCatalogClient;

    public CommonPortalServiceImpl(@Autowired FhCatalogClient fhCatalogClient) {
        this.fhCatalogClient = fhCatalogClient;
    }

    @Override
    public Map<String, String> getNameMapping() {

        return fhCatalogClient.getParticipantDetails().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getName()));
    }
}
