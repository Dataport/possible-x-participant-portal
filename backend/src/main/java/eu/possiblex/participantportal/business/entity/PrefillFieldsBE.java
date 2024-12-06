package eu.possiblex.participantportal.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrefillFieldsBE {
    String participantId;

    DataProductPrefillFieldsBE dataProductPrefillFields;
}
