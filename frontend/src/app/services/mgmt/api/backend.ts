/* tslint:disable */
/* eslint-disable */

export interface ICommonPortalRestApi {
    version: IVersionTO;
}

export interface IConsumerRestApi {
}

export interface IContractRestApi {
    contractAgreements: IContractAgreementTO[];
}

export interface IParticipantRestApi {
    participantDetails: IParticipantDetailsTO;
    participantId: IParticipantIdTO;
}

export interface IProviderRestApi {
}

export interface IResourceShapeRestApi {
    gxInstantiatedVirtualResourceShape: string;
    gxDataResourceShape: string;
    gxPhysicalResourceShape: string;
    gxSoftwareResourceShape: string;
    gxVirtualResourceShape: string;
    gxLegitimateInterestShape: string;
}

export interface IServiceOfferingShapeRestApi {
    gxServiceOfferingShape: string;
}

export interface IAcceptOfferResponseTO {
    negotiationState: INegotiationState;
    contractAgreementId: string;
    dataOffering: boolean;
}

export interface IAcceptOfferResponseTOBuilder {
}

export interface IAssetDetailsTO {
    name: string;
    description: string;
}

export interface IAssetDetailsTOBuilder {
}

export interface IConsumeOfferRequestTO {
    counterPartyAddress: string;
    edcOfferId: string;
    dataOffering: boolean;
}

export interface IConsumeOfferRequestTOBuilder {
}

export interface IContractAgreementTO {
    id: string;
    assetId: string;
    assetDetails: IAssetDetailsTO;
    policy: IPolicy;
    contractSigningDate: Date;
    consumerId: string;
    providerId: string;
}

export interface IContractAgreementTOBuilder {
}

export interface ICreateDataOfferingRequestTO extends ICreateServiceOfferingRequestTO {
    dataResourceCredentialSubject: IGxDataResourceCredentialSubject;
    fileName: string;
    legitimateInterest: IGxLegitimateInterest;
}

export interface ICreateDataOfferingRequestTOBuilder<C, B> extends ICreateServiceOfferingRequestTOBuilder<C, B> {
}

export interface ICreateDataOfferingRequestTOBuilderImpl extends ICreateDataOfferingRequestTOBuilder<ICreateDataOfferingRequestTO, ICreateDataOfferingRequestTOBuilderImpl> {
}

export interface ICreateOfferResponseTO {
    edcResponseId: string;
    fhResponseId: string;
}

export interface ICreateOfferResponseTOBuilder {
}

export interface ICreateServiceOfferingRequestTO {
    serviceOfferingCredentialSubject: IGxServiceOfferingCredentialSubject;
    enforcementPolicies: IEnforcementPolicyUnion[];
}

export interface ICreateServiceOfferingRequestTOBuilder<C, B> {
}

export interface ICreateServiceOfferingRequestTOBuilderImpl extends ICreateServiceOfferingRequestTOBuilder<ICreateServiceOfferingRequestTO, ICreateServiceOfferingRequestTOBuilderImpl> {
}

export interface IOfferDetailsTO {
    edcOfferId: string;
    catalogOffering: IPxExtendedServiceOfferingCredentialSubject;
    dataOffering: boolean;
    enforcementPolicies: IEnforcementPolicyUnion[];
}

export interface IOfferDetailsTOBuilder {
}

export interface IParticipantDetailsTO {
    participantId: string;
    participantName: string;
    participantEmail: string;
}

export interface IParticipantDetailsTOBuilder {
}

export interface IParticipantIdTO {
    participantId: string;
}

export interface IParticipantIdTOBuilder {
}

export interface ISelectOfferRequestTO {
    fhCatalogOfferId: string;
}

export interface ISelectOfferRequestTOBuilder {
}

export interface ITransferOfferRequestTO {
    contractAgreementId: string;
    counterPartyAddress: string;
    edcOfferId: string;
}

export interface ITransferOfferRequestTOBuilder {
}

export interface ITransferOfferResponseTO {
    transferProcessState: ITransferProcessState;
}

export interface ITransferOfferResponseTOBuilder {
}

export interface IVersionTO {
    version: string;
}

export interface IVersionTOBuilder {
}

export interface IPojoCredentialSubject {
    "@type": "UnknownCredentialSubject" | "gx:DataResource" | "gx:ServiceOffering";
    id: string;
}

export interface IPojoCredentialSubjectBuilder<C, B> {
}

export interface IUnknownCredentialSubject extends IPojoCredentialSubject {
    "@type": "UnknownCredentialSubject";
}

export interface IUnknownCredentialSubjectBuilder<C, B> extends IPojoCredentialSubjectBuilder<C, B> {
}

export interface IUnknownCredentialSubjectBuilderImpl extends IUnknownCredentialSubjectBuilder<IUnknownCredentialSubject, IUnknownCredentialSubjectBuilderImpl> {
}

export interface IGxDataAccountExport {
    "gx:requestType": string;
    "gx:accessType": string;
    "gx:formatType": string;
}

export interface IGxDataAccountExportBuilder {
    "gx:requestType": string;
    "gx:accessType": string;
    "gx:formatType": string;
}

export interface IGxSOTermsAndConditions {
    "gx:URL": string;
    "gx:hash": string;
}

export interface IGxSOTermsAndConditionsBuilder {
    "gx:URL": string;
    "gx:hash": string;
}

export interface INodeKindIRITypeId {
    id: string;
}

export interface IGxDataResourceCredentialSubject extends IPojoCredentialSubject {
    "@type": "gx:DataResource";
    "gx:copyrightOwnedBy": INodeKindIRITypeId;
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:containsPII": boolean;
    "schema:name": string;
    "schema:description": string;
    "@context": { [index: string]: string };
    type: string;
}

export interface IGxDataResourceCredentialSubjectBuilder<C, B> extends IPojoCredentialSubjectBuilder<C, B> {
}

export interface IGxDataResourceCredentialSubjectBuilderImpl extends IGxDataResourceCredentialSubjectBuilder<IGxDataResourceCredentialSubject, IGxDataResourceCredentialSubjectBuilderImpl> {
    "gx:copyrightOwnedBy": INodeKindIRITypeId;
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:containsPII": boolean;
    "schema:name": string;
    "schema:description": string;
}

export interface IGxLegitimateInterest {
    "@type": string;
    "gx:dataProtectionContact": string;
    "gx:legalBasis": string;
}

export interface IGxLegitimateInterestBuilder<C, B> {
}

export interface IGxLegitimateInterestBuilderImpl extends IGxLegitimateInterestBuilder<IGxLegitimateInterest, IGxLegitimateInterestBuilderImpl> {
    "gx:dataProtectionContact": string;
    "gx:legalBasis": string;
    "@type": string;
}

export interface IGxServiceOfferingCredentialSubject extends IPojoCredentialSubject {
    "@type": "gx:ServiceOffering";
    "gx:providedBy": INodeKindIRITypeId;
    "gx:aggregationOf": INodeKindIRITypeId[];
    "gx:termsAndConditions": IGxSOTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IGxDataAccountExport[];
    "schema:name": string;
    "schema:description": string;
    "@context": { [index: string]: string };
    type: string;
}

export interface IGxServiceOfferingCredentialSubjectBuilder<C, B> extends IPojoCredentialSubjectBuilder<C, B> {
}

export interface IGxServiceOfferingCredentialSubjectBuilderImpl extends IGxServiceOfferingCredentialSubjectBuilder<IGxServiceOfferingCredentialSubject, IGxServiceOfferingCredentialSubjectBuilderImpl> {
    "gx:providedBy": INodeKindIRITypeId;
    "gx:aggregationOf": INodeKindIRITypeId[];
    "gx:termsAndConditions": IGxSOTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IGxDataAccountExport[];
    "schema:name": string;
    "schema:description": string;
}

export interface IOfferingComplianceException extends IException {
}

export interface IEnforcementPolicy {
    "@type": "EverythingAllowedPolicy" | "ParticipantRestrictionPolicy";
}

export interface IEnforcementPolicyBuilder<C, B> {
}

export interface IEverythingAllowedPolicy extends IEnforcementPolicy {
    "@type": "EverythingAllowedPolicy";
}

export interface IEverythingAllowedPolicyBuilder<C, B> extends IEnforcementPolicyBuilder<C, B> {
}

export interface IEverythingAllowedPolicyBuilderImpl extends IEverythingAllowedPolicyBuilder<IEverythingAllowedPolicy, IEverythingAllowedPolicyBuilderImpl> {
}

export interface IParticipantRestrictionPolicy extends IEnforcementPolicy {
    "@type": "ParticipantRestrictionPolicy";
    allowedParticipants: string[];
}

export interface IParticipantRestrictionPolicyBuilder<C, B> extends IEnforcementPolicyBuilder<C, B> {
}

export interface IParticipantRestrictionPolicyBuilderImpl extends IParticipantRestrictionPolicyBuilder<IParticipantRestrictionPolicy, IParticipantRestrictionPolicyBuilderImpl> {
}

export interface IPolicy {
    "@id": string;
    "odrl:permission": IOdrlPermission[];
    "odrl:prohibition": any[];
    "odrl:obligation": any[];
    "odrl:target": IPolicyTarget;
    "@context": string;
    "@type": string;
}

export interface IPxExtendedServiceOfferingCredentialSubject {
    id: string;
    "gx:providedBy": INodeKindIRITypeId;
    "gx:aggregationOf": IPxExtendedDataResourceCredentialSubject[];
    "gx:termsAndConditions": IGxSOTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IGxDataAccountExport[];
    "schema:name": string;
    "schema:description": string;
    "px:assetId": string;
    "px:providerUrl": string;
    "@context": { [index: string]: string };
    "@type": string[];
}

export interface IThrowable extends ISerializable {
    cause: IThrowable;
    stackTrace: IStackTraceElement[];
    message: string;
    suppressed: IThrowable[];
    localizedMessage: string;
}

export interface IStackTraceElement extends ISerializable {
    classLoaderName: string;
    moduleName: string;
    moduleVersion: string;
    methodName: string;
    fileName: string;
    lineNumber: number;
    nativeMethod: boolean;
    className: string;
}

export interface IException extends IThrowable {
}

export interface IOdrlPermission {
    "odrl:target": string;
    "odrl:action": IOdrlAction;
    "odrl:constraint": IOdrlConstraint[];
}

export interface IPolicyTarget {
    "@id": string;
}

export interface IPxExtendedDataResourceCredentialSubject {
    id: string;
    "gx:copyrightOwnedBy": INodeKindIRITypeId;
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:containsPII": boolean;
    "gx:legitimateInterest": IGxLegitimateInterest;
    "schema:name": string;
    "schema:description": string;
    "@context": { [index: string]: string };
    "@type": string[];
}

export interface ISerializable {
}

export interface IOdrlConstraint {
    "odrl:leftOperand": string;
    "odrl:operator": IOdrlOperator;
    "odrl:rightOperand": string;
    "@type": string;
}

export interface HttpClient {

    request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; }): RestResponse<R>;
}

export class RestApplicationClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP GET /common/version
     * Java method: eu.possiblex.participantportal.application.boundary.CommonPortalRestApiImpl.getVersion
     */
    getVersion(): RestResponse<IVersionTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`common/version` });
    }

    /**
     * HTTP POST /consumer/offer/accept
     * Java method: eu.possiblex.participantportal.application.boundary.ConsumerRestApiImpl.acceptContractOffer
     */
    acceptContractOffer(request: IConsumeOfferRequestTO): RestResponse<IAcceptOfferResponseTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`consumer/offer/accept`, data: request });
    }

    /**
     * HTTP POST /consumer/offer/select
     * Java method: eu.possiblex.participantportal.application.boundary.ConsumerRestApiImpl.selectContractOffer
     */
    selectContractOffer(request: ISelectOfferRequestTO): RestResponse<IOfferDetailsTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`consumer/offer/select`, data: request });
    }

    /**
     * HTTP POST /consumer/offer/transfer
     * Java method: eu.possiblex.participantportal.application.boundary.ConsumerRestApiImpl.transferDataOffer
     */
    transferDataOffer(request: ITransferOfferRequestTO): RestResponse<ITransferOfferResponseTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`consumer/offer/transfer`, data: request });
    }

    /**
     * HTTP GET /contract/agreement
     * Java method: eu.possiblex.participantportal.application.boundary.ContractRestApiImpl.getContractAgreements
     */
    getContractAgreements(): RestResponse<IContractAgreementTO[]> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`contract/agreement` });
    }

    /**
     * HTTP POST /contract/transfer
     * Java method: eu.possiblex.participantportal.application.boundary.ContractRestApiImpl.transferDataOfferAgain
     */
    transferDataOfferAgain(request: ITransferOfferRequestTO): RestResponse<ITransferOfferResponseTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`contract/transfer`, data: request });
    }

    /**
     * HTTP GET /participant/details/me
     * Java method: eu.possiblex.participantportal.application.boundary.ParticipantRestApiImpl.getParticipantDetails
     */
    getParticipantDetails$GET$participant_details_me(): RestResponse<IParticipantDetailsTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`participant/details/me` });
    }

    /**
     * HTTP GET /participant/details/{participantId}
     * Java method: eu.possiblex.participantportal.application.boundary.ParticipantRestApiImpl.getParticipantDetails
     */
    getParticipantDetails$GET$participant_details_participantId(participantId: string): RestResponse<IParticipantDetailsTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`participant/details/${participantId}` });
    }

    /**
     * HTTP GET /participant/id/me
     * Java method: eu.possiblex.participantportal.application.boundary.ParticipantRestApiImpl.getParticipantId
     */
    getParticipantId(): RestResponse<IParticipantIdTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`participant/id/me` });
    }

    /**
     * HTTP POST /provider/offer/data
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApiImpl.createDataOffering
     */
    createDataOffering(createDataOfferingRequestTO: ICreateDataOfferingRequestTO): RestResponse<ICreateOfferResponseTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`provider/offer/data`, data: createDataOfferingRequestTO });
    }

    /**
     * HTTP POST /provider/offer/service
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApiImpl.createServiceOffering
     */
    createServiceOffering(createServiceOfferingRequestTO: ICreateServiceOfferingRequestTO): RestResponse<ICreateOfferResponseTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`provider/offer/service`, data: createServiceOfferingRequestTO });
    }

    /**
     * HTTP GET /shapes/gx/resource/dataresource
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxDataResourceShape
     */
    getGxDataResourceShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/resource/dataresource` });
    }

    /**
     * HTTP GET /shapes/gx/resource/instantiatedvirtualresource
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxInstantiatedVirtualResourceShape
     */
    getGxInstantiatedVirtualResourceShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/resource/instantiatedvirtualresource` });
    }

    /**
     * HTTP GET /shapes/gx/resource/legitimateinterest
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxLegitimateInterestShape
     */
    getGxLegitimateInterestShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/resource/legitimateinterest` });
    }

    /**
     * HTTP GET /shapes/gx/resource/physicalresource
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxPhysicalResourceShape
     */
    getGxPhysicalResourceShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/resource/physicalresource` });
    }

    /**
     * HTTP GET /shapes/gx/resource/softwareresource
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxSoftwareResourceShape
     */
    getGxSoftwareResourceShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/resource/softwareresource` });
    }

    /**
     * HTTP GET /shapes/gx/resource/virtualresource
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxVirtualResourceShape
     */
    getGxVirtualResourceShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/resource/virtualresource` });
    }

    /**
     * HTTP GET /shapes/gx/serviceoffering
     * Java method: eu.possiblex.participantportal.application.boundary.ShapeRestApiImpl.getGxServiceOfferingShape
     */
    getGxServiceOfferingShape(): RestResponse<string> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`shapes/gx/serviceoffering` });
    }
}

export type RestResponse<R> = Promise<R>;

export type INegotiationState = "INITIAL" | "REQUESTING" | "REQUESTED" | "OFFERING" | "OFFERED" | "ACCEPTING" | "ACCEPTED" | "AGREEING" | "AGREED" | "VERIFYING" | "VERIFIED" | "FINALIZING" | "FINALIZED" | "TERMINATING" | "TERMINATED";

export type ITransferProcessState = "INITIAL" | "PROVISIONING" | "PROVISIONING_REQUESTED" | "PROVISIONED" | "REQUESTING" | "REQUESTED" | "STARTING" | "STARTED" | "SUSPENDING" | "SUSPENDED" | "COMPLETING" | "COMPLETED" | "TERMINATING" | "TERMINATED" | "DEPROVISIONING" | "DEPROVISIONING_REQUESTED" | "DEPROVISIONED";

export type IOdrlAction = "use" | "transfer";

export type IOdrlOperator = "odrl:eq" | "odrl:neq" | "odrl:isPartOf" | "odrl:isAnyOf";

export type IPojoCredentialSubjectUnion = IGxDataResourceCredentialSubject | IGxServiceOfferingCredentialSubject;

export type IEnforcementPolicyUnion = IEverythingAllowedPolicy | IParticipantRestrictionPolicy;

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
