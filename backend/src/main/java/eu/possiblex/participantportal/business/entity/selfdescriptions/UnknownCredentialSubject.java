package eu.possiblex.participantportal.business.entity.selfdescriptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = { "type", "@context" }, allowGetters = true)
public class UnknownCredentialSubject extends PojoCredentialSubject {
    // no fields as we do not know the structure of the credential subject
}
