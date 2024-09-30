package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.selfdescriptions.px.PxExtendedServiceOfferingCredentialSubject;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProviderServiceMapper {

    @Mapping(target = "providedBy", source = "request.providedBy")
    @Mapping(target = "aggregationOf", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "termsAndConditions", source = "request.termsAndConditions")
    @Mapping(target = "policy", source = "request.policy", qualifiedByName = "policyToStringList")
    @Mapping(target = "dataProtectionRegime", source = "request.dataProtectionRegime")
    @Mapping(target = "dataAccountExport", source = "request.dataAccountExport")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "assetId", source = "assetId")
    @Mapping(target = "providerUrl", source = "providerUrl")
    @Mapping(target = "id", source = "offeringId")
    @Mapping(target = "schemaName", source = "request.name")
    @Mapping(target = "schemaDescription", source = "request.description")
    PxExtendedServiceOfferingCredentialSubject getPxExtendedServiceOfferingCredentialSubject(
        CreateServiceOfferingRequestBE request, String offeringId, String assetId, String providerUrl);

    @InheritConfiguration
    @Mapping(target = "aggregationOf", expression = "java(java.util.List.of(request.getDataResource()))")
    PxExtendedServiceOfferingCredentialSubject getPxExtendedServiceOfferingCredentialSubject(
        CreateDataOfferingRequestBE request, String offeringId, String assetId, String providerUrl);

    @Mapping(target = "assetId", source = "assetId")
    @Mapping(target = "properties.offerId", source = "offerId")
    @Mapping(target = "properties.name", source = "request.name")
    @Mapping(target = "properties.description", source = "request.description")
    @Mapping(target = "properties.providedBy", source = "request.providedBy")
    @Mapping(target = "properties.termsAndConditions", source = "request.termsAndConditions")
    @Mapping(target = "properties.dataProtectionRegime", source = "request.dataProtectionRegime")
    @Mapping(target = "properties.dataAccountExport", source = "request.dataAccountExport")
    @Mapping(target = "properties.contenttype", ignore = true)
    @Mapping(target = "properties.version", ignore = true)
    @Mapping(target = "fileName", constant = "")
    @Mapping(target = "policy", source = "request.policy")
    CreateEdcOfferBE getCreateEdcOfferBE(CreateServiceOfferingRequestBE request, String offerId, String assetId);

    @InheritConfiguration
    @Mapping(target = "properties.copyrightOwnedBy", source = "request.dataResource.copyrightOwnedBy")
    @Mapping(target = "properties.producedBy", source = "request.dataResource.producedBy")
    @Mapping(target = "properties.exposedThrough", source = "request.dataResource.exposedThrough")
    @Mapping(target = "properties.license", source = "request.dataResource.license")
    @Mapping(target = "properties.containsPII", source = "request.dataResource.containsPII")
    @Mapping(target = "fileName", source = "request.fileName")
    CreateEdcOfferBE getCreateEdcOfferBE(CreateDataOfferingRequestBE request, String offerId, String assetId);

    @Named("policyToStringList")
    default List<String> policyToStringList(Policy policy) {

        try {
            return List.of(new ObjectMapper().writeValueAsString(policy));
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

}
