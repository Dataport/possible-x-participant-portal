package eu.possible_x.edc_orchestrator.entities.fh;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FhConstants {
    public static final Map<String, String> FH_CONTEXT = Map.of(
            "skos", "http://www.w3.org/2004/02/skos/core#",
            "dct", "http://purl.org/dc/terms/",
            "dcat", "http://www.w3.org/ns/dcat#",
            "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            "foaf", "http://xmlns.com/foaf/0.1/");

    public static final ArrayList<String> FH_TYPE_DATASET = new ArrayList<>(List.of(
            "http://w3id.org/gaia-x/gax-trust-framework#DataResource ",
            "dcat:Dataset"));

}
