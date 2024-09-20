package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.px.PxExtendedServiceOfferingCredentialSubject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderServiceMapper {

    @Mapping(target = "providedBy", source = "gxServiceOfferingCredentialSubject.providedBy")
    @Mapping(target = "aggregationOf", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "termsAndConditions", source = "gxServiceOfferingCredentialSubject.termsAndConditions")
    @Mapping(target = "policy", source = "gxServiceOfferingCredentialSubject.policy")
    @Mapping(target = "dataProtectionRegime", source = "gxServiceOfferingCredentialSubject.dataProtectionRegime")
    @Mapping(target = "dataAccountExport", source = "gxServiceOfferingCredentialSubject.dataAccountExport")
    @Mapping(target = "name", source = "gxServiceOfferingCredentialSubject.name")
    @Mapping(target = "description", source = "gxServiceOfferingCredentialSubject.description")
    @Mapping(target = "assetId", source = "assetId")
    @Mapping(target = "providerUrl", source = "providerUrl")
    PxExtendedServiceOfferingCredentialSubject getExtendedServiceOfferingCredentialSubject(
        GxServiceOfferingCredentialSubject gxServiceOfferingCredentialSubject, String assetId, String providerUrl);

    @Mapping(target = "providedBy", source = "gxServiceOfferingCredentialSubject.providedBy")
    @Mapping(target = "aggregationOf", expression = "java(java.util.List.of(gxDataResourceCredentialSubject))")
    @Mapping(target = "termsAndConditions", source = "gxServiceOfferingCredentialSubject.termsAndConditions")
    @Mapping(target = "policy", source = "gxServiceOfferingCredentialSubject.policy")
    @Mapping(target = "dataProtectionRegime", source = "gxServiceOfferingCredentialSubject.dataProtectionRegime")
    @Mapping(target = "dataAccountExport", source = "gxServiceOfferingCredentialSubject.dataAccountExport")
    @Mapping(target = "name", source = "gxServiceOfferingCredentialSubject.name")
    @Mapping(target = "description", source = "gxServiceOfferingCredentialSubject.description")
    @Mapping(target = "assetId", source = "assetId")
    @Mapping(target = "providerUrl", source = "providerUrl")
    PxExtendedServiceOfferingCredentialSubject getExtendedServiceOfferingCredentialSubject(
        GxServiceOfferingCredentialSubject gxServiceOfferingCredentialSubject,
        GxDataResourceCredentialSubject gxDataResourceCredentialSubject, String assetId, String providerUrl);
}
