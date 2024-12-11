package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferingDetailsSparqlQueryResult {
    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String uri;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String assetId;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String name;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String description;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String providerUrl;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String aggregationOf;

    @JsonDeserialize(using = CatalogLiteralTnCListDeserializer.class)
    private List<TermsAndConditions> tncList;
}
