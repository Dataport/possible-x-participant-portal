/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/* tslint:disable */
/* eslint-disable */

export interface ICommonPortalRestApi {
    nameMapping: { [index: string]: string };
    version: IVersionTO;
}

export interface IConsumerRestApi {
}

export interface IContractRestApi {
    contractAgreements: IContractAgreementTO[];
}

export interface IProviderRestApi {
    prefillFields: IPrefillFieldsTO;
}

export interface IResourceShapeRestApi {
    gxPhysicalResourceShape: string;
    gxSoftwareResourceShape: string;
    gxVirtualResourceShape: string;
    gxLegitimateInterestShape: string;
    gxDataResourceShape: string;
    gxInstantiatedVirtualResourceShape: string;
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
    assetId: string;
    offeringId: string;
    providerUrl: string;
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
    enforcementPolicies: IEnforcementPolicyUnion[];
    contractSigningDate: Date;
    consumerDetails: IContractParticipantDetailsTO;
    providerDetails: IContractParticipantDetailsTO;
    provider: boolean;
    dataOffering: boolean;
}

export interface IContractAgreementTOBuilder {
}

export interface IContractDetailsTO {
    id: string;
    assetId: string;
    catalogOffering: IPxExtendedServiceOfferingCredentialSubject;
    offerRetrievalDate: Date;
    policy: IPolicy;
    enforcementPolicies: IEnforcementPolicyUnion[];
    contractSigningDate: Date;
    consumerDetails: IContractParticipantDetailsTO;
    providerDetails: IContractParticipantDetailsTO;
    dataOffering: boolean;
}

export interface IContractDetailsTOBuilder {
}

export interface IContractParticipantDetailsTO {
    name: string;
    did: string;
    dapsId: string;
}

export interface IContractParticipantDetailsTOBuilder {
}

export interface ICreateDataOfferingRequestTO extends ICreateServiceOfferingRequestTO {
    dataResourceCredentialSubject: IGxDataResourceCredentialSubject;
    fileName: string;
    legitimateInterestCredentialSubject: IGxLegitimateInterestCredentialSubject;
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

export interface IDataServiceOfferingPrefillFieldsTO {
    serviceOfferingName: string;
    serviceOfferingDescription: string;
}

export interface IDataServiceOfferingPrefillFieldsTOBuilder {
}

export interface IErrorResponseTO {
    timestamp: Date;
    message: string;
    details: string;
}

export interface IOfferDetailsTO {
    edcOfferId: string;
    catalogOffering: IPxExtendedServiceOfferingCredentialSubject;
    dataOffering: boolean;
    enforcementPolicies: IEnforcementPolicyUnion[];
    providerDetails: IParticipantDetailsTO;
    offerRetrievalDate: Date;
}

export interface IOfferDetailsTOBuilder {
}

export interface IOfferWithTimestampTO {
    catalogOffering: IPxExtendedServiceOfferingCredentialSubject;
    offerRetrievalDate: Date;
}

export interface IOfferWithTimestampTOBuilder {
}

export interface IParticipantDetailsTO extends IParticipantNameTO {
    participantEmail: string;
}

export interface IParticipantDetailsTOBuilder<C, B> extends IParticipantNameTOBuilder<C, B> {
}

export interface IParticipantDetailsTOBuilderImpl extends IParticipantDetailsTOBuilder<IParticipantDetailsTO, IParticipantDetailsTOBuilderImpl> {
}

export interface IParticipantNameTO {
    participantId: string;
    participantName: string;
}

export interface IParticipantNameTOBuilder<C, B> {
}

export interface IParticipantNameTOBuilderImpl extends IParticipantNameTOBuilder<IParticipantNameTO, IParticipantNameTOBuilderImpl> {
}

export interface IPrefillFieldsTO {
    participantId: string;
    dataServiceOfferingPrefillFields: IDataServiceOfferingPrefillFieldsTO;
}

export interface IPrefillFieldsTOBuilder {
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
    date: string;
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
    "gx:copyrightOwnedBy": string[];
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:containsPII": boolean;
    "schema:name": string;
    "schema:description": string;
    /**
     * JSON-LD context
     */
    "@context": { [index: string]: string };
    /**
     * JSON-LD type
     */
    type: string;
}

export interface IGxDataResourceCredentialSubjectBuilder<C, B> extends IPojoCredentialSubjectBuilder<C, B> {
}

export interface IGxDataResourceCredentialSubjectBuilderImpl extends IGxDataResourceCredentialSubjectBuilder<IGxDataResourceCredentialSubject, IGxDataResourceCredentialSubjectBuilderImpl> {
    "gx:copyrightOwnedBy": string[];
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:containsPII": boolean;
    "schema:name": string;
    "schema:description": string;
}

export interface IGxLegitimateInterestCredentialSubject {
    "gx:dataProtectionContact": string;
    "gx:legalBasis": string;
    /**
     * JSON-LD type
     */
    type: string;
}

export interface IGxLegitimateInterestCredentialSubjectBuilder<C, B> {
}

export interface IGxLegitimateInterestCredentialSubjectBuilderImpl extends IGxLegitimateInterestCredentialSubjectBuilder<IGxLegitimateInterestCredentialSubject, IGxLegitimateInterestCredentialSubjectBuilderImpl> {
    "gx:dataProtectionContact": string;
    "gx:legalBasis": string;
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
    /**
     * JSON-LD context
     */
    "@context": { [index: string]: string };
    /**
     * JSON-LD type
     */
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

/**
 * Policy that restricts the transfer period
 */
export interface IEndAgreementOffsetPolicy extends ITimeAgreementOffsetPolicy {
    "@type": "EndAgreementOffsetPolicy";
}

export interface IEndAgreementOffsetPolicyBuilder<C, B> extends ITimeAgreementOffsetPolicyBuilder<C, B> {
}

export interface IEndAgreementOffsetPolicyBuilderImpl extends IEndAgreementOffsetPolicyBuilder<IEndAgreementOffsetPolicy, IEndAgreementOffsetPolicyBuilderImpl> {
}

/**
 * Policy that restricts the contract duration to an end date
 */
export interface IEndDatePolicy extends ITimeDatePolicy {
    "@type": "EndDatePolicy";
}

export interface IEndDatePolicyBuilder<C, B> extends ITimeDatePolicyBuilder<C, B> {
}

export interface IEndDatePolicyBuilderImpl extends IEndDatePolicyBuilder<IEndDatePolicy, IEndDatePolicyBuilderImpl> {
}

export interface IEnforcementPolicy {
    "@type": "EverythingAllowedPolicy" | "ParticipantRestrictionPolicy" | "EndAgreementOffsetPolicy" | "StartAgreementOffsetPolicy" | "EndDatePolicy" | "StartDatePolicy";
    valid: boolean;
}

export interface IEnforcementPolicyBuilder<C, B> {
}

/**
 * Policy that allows everything
 */
export interface IEverythingAllowedPolicy extends IEnforcementPolicy {
    "@type": "EverythingAllowedPolicy";
}

export interface IEverythingAllowedPolicyBuilder<C, B> extends IEnforcementPolicyBuilder<C, B> {
}

export interface IEverythingAllowedPolicyBuilderImpl extends IEverythingAllowedPolicyBuilder<IEverythingAllowedPolicy, IEverythingAllowedPolicyBuilderImpl> {
}

/**
 * Policy that restricts the contractual booking to specific participants
 */
export interface IParticipantRestrictionPolicy extends IEnforcementPolicy {
    "@type": "ParticipantRestrictionPolicy";
    allowedParticipants: string[];
}

export interface IParticipantRestrictionPolicyBuilder<C, B> extends IEnforcementPolicyBuilder<C, B> {
}

export interface IParticipantRestrictionPolicyBuilderImpl extends IParticipantRestrictionPolicyBuilder<IParticipantRestrictionPolicy, IParticipantRestrictionPolicyBuilderImpl> {
}

export interface IStartAgreementOffsetPolicy extends ITimeAgreementOffsetPolicy {
    "@type": "StartAgreementOffsetPolicy";
}

export interface IStartAgreementOffsetPolicyBuilder<C, B> extends ITimeAgreementOffsetPolicyBuilder<C, B> {
}

export interface IStartAgreementOffsetPolicyBuilderImpl extends IStartAgreementOffsetPolicyBuilder<IStartAgreementOffsetPolicy, IStartAgreementOffsetPolicyBuilderImpl> {
}

/**
 * Policy that restricts the contract duration to a start date
 */
export interface IStartDatePolicy extends ITimeDatePolicy {
    "@type": "StartDatePolicy";
}

export interface IStartDatePolicyBuilder<C, B> extends ITimeDatePolicyBuilder<C, B> {
}

export interface IStartDatePolicyBuilderImpl extends IStartDatePolicyBuilder<IStartDatePolicy, IStartDatePolicyBuilderImpl> {
}

export interface ITimeAgreementOffsetPolicy extends IEnforcementPolicy {
    "@type": "EndAgreementOffsetPolicy" | "StartAgreementOffsetPolicy";
    offsetNumber: number;
    offsetUnit: IAgreementOffsetUnit;
}

export interface ITimeAgreementOffsetPolicyBuilder<C, B> extends IEnforcementPolicyBuilder<C, B> {
}

export interface ITimeDatePolicy extends IEnforcementPolicy {
    "@type": "EndDatePolicy" | "StartDatePolicy";
    date: Date;
}

export interface ITimeDatePolicyBuilder<C, B> extends IEnforcementPolicyBuilder<C, B> {
}

export interface ILegitimateInterestValidator extends IConstraintValidator<IValidLegitimateInterestForPII, ICreateDataOfferingRequestTO> {
}

export interface IPolicy {
    "@id": string;
    "odrl:permission": IOdrlPermission[];
    "odrl:prohibition": any[];
    "odrl:obligation": any[];
    "odrl:target": IPolicyTarget;
    /**
     * JSON-LD context
     */
    "@context": string;
    /**
     * JSON-LD type
     */
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
    /**
     * JSON-LD context
     */
    "@context": { [index: string]: string };
    /**
     * JSON-LD type
     */
    "@type": string[];
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
    "gx:copyrightOwnedBy": string[];
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:containsPII": boolean;
    "gx:legitimateInterest": IGxLegitimateInterestCredentialSubject;
    "schema:name": string;
    "schema:description": string;
    /**
     * JSON-LD context
     */
    "@context": { [index: string]: string };
    /**
     * JSON-LD type
     */
    "@type": string[];
}

export interface IConstraintValidator<A, T> {
}

export interface IValidLegitimateInterestForPII extends IAnnotation {
}

export interface IOdrlConstraint {
    "odrl:leftOperand": string;
    "odrl:operator": IOdrlOperator;
    "odrl:rightOperand": string;
    /**
     * JSON-LD type
     */
    "@type": string;
}

export interface IAnnotation {
}

export interface HttpClient {

    request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; }): RestResponse<R>;
}

export class RestApplicationClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP GET /common/participant/name-mapping
     * Java method: eu.possiblex.participantportal.application.boundary.CommonPortalRestApiImpl.getNameMapping
     */
    getNameMapping(): RestResponse<{ [index: string]: string }> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`common/participant/name-mapping` });
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
     * HTTP GET /contract/details/{contractAgreementId}
     * Java method: eu.possiblex.participantportal.application.boundary.ContractRestApiImpl.getContractDetailsByContractAgreementId
     */
    getContractDetailsByContractAgreementId(contractAgreementId: string): RestResponse<IContractDetailsTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`contract/details/${contractAgreementId}` });
    }

    /**
     * HTTP GET /contract/details/{contractAgreementId}/offer
     * Java method: eu.possiblex.participantportal.application.boundary.ContractRestApiImpl.getOfferWithTimestampByContractAgreementId
     */
    getOfferWithTimestampByContractAgreementId(contractAgreementId: string): RestResponse<IOfferWithTimestampTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`contract/details/${contractAgreementId}/offer` });
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
     * HTTP GET /provider/prefillFields
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApiImpl.getPrefillFields
     */
    getPrefillFields(): RestResponse<IPrefillFieldsTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`provider/prefillFields` });
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

export type IAgreementOffsetUnit = "s" | "m" | "h" | "d";

export type INegotiationState = "INITIAL" | "REQUESTING" | "REQUESTED" | "OFFERING" | "OFFERED" | "ACCEPTING" | "ACCEPTED" | "AGREEING" | "AGREED" | "VERIFYING" | "VERIFIED" | "FINALIZING" | "FINALIZED" | "TERMINATING" | "TERMINATED";

export type ITransferProcessState = "INITIAL" | "PROVISIONING" | "PROVISIONING_REQUESTED" | "PROVISIONED" | "REQUESTING" | "REQUESTED" | "STARTING" | "STARTED" | "SUSPENDING" | "SUSPENDED" | "COMPLETING" | "COMPLETED" | "TERMINATING" | "TERMINATED" | "DEPROVISIONING" | "DEPROVISIONING_REQUESTED" | "DEPROVISIONED";

export type IOdrlAction = "http://www.w3.org/ns/odrl/2/use" | "http://www.w3.org/ns/odrl/2/transfer";

export type IOdrlOperator = "odrl:eq" | "odrl:gteq" | "odrl:lteq" | "odrl:neq" | "odrl:isPartOf" | "odrl:isAnyOf";

export type IPojoCredentialSubjectUnion = IGxDataResourceCredentialSubject | IGxServiceOfferingCredentialSubject;

export type IEnforcementPolicyUnion = IEverythingAllowedPolicy | IParticipantRestrictionPolicy | IStartDatePolicy | IEndDatePolicy | IEndAgreementOffsetPolicy;

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
