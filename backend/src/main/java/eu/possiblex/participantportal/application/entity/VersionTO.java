package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionTO {
    /**
     * the version of the participant portal
     */
    private String version;

    /**
     * the date of the version of the participant portal
     */
    private String date;
}
