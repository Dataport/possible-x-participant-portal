/* tslint:disable */
/* eslint-disable */

export interface IConsumerRestApi {
}

export interface IProviderRestApi {
    participantId: IParticipantIdTO;
}

export interface IResourceShapeRestApi {
    gxInstantiatedVirtualResourceShape: string;
    gxDataResourceShape: string;
    gxPhysicalResourceShape: string;
    gxSoftwareResourceShape: string;
    gxVirtualResourceShape: string;
}

export interface IServiceOfferingShapeRestApi {
    gxServiceOfferingShape: string;
}

export interface IConsumeOfferRequestTO {
    counterPartyAddress: string;
    edcOfferId: string;
}

export interface IConsumeOfferRequestTOBuilder {
}

export interface ICreateDataOfferingRequestTO extends ICreateServiceOfferingRequestTO {
    dataResourceCredentialSubject: IGxDataResourceCredentialSubject;
    fileName: string;
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
    policy: IPolicy;
}

export interface ICreateServiceOfferingRequestTOBuilder<C, B> {
}

export interface ICreateServiceOfferingRequestTOBuilderImpl extends ICreateServiceOfferingRequestTOBuilder<ICreateServiceOfferingRequestTO, ICreateServiceOfferingRequestTOBuilderImpl> {
}

export interface IOfferDetailsTO {
    edcOfferId: string;
    counterPartyAddress: string;
    offerType: string;
    creationDate: Date;
    name: string;
    description: string;
    contentType: string;
}

export interface IOfferDetailsTOBuilder {
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

export interface ITransferDetailsTO {
    state: ITransferProcessState;
}

export interface ITransferDetailsTOBuilder {
}

export interface IPojoCredentialSubject {
    "@type": "UnknownCredentialSubject" | "gx:DataResource" | "gx:ServiceOffering" | "PxExtendedServiceOfferingCredentialSubject";
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
    "gx:name": string;
    "gx:description": string;
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
    "gx:name": string;
    "gx:description": string;
}

export interface IGxServiceOfferingCredentialSubject extends IPojoCredentialSubject {
    "@type": "gx:ServiceOffering";
    "gx:providedBy": INodeKindIRITypeId;
    "gx:aggregationOf": INodeKindIRITypeId[];
    "gx:termsAndConditions": IGxSOTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IGxDataAccountExport[];
    "gx:name": string;
    "gx:description": string;
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
    "gx:name": string;
    "gx:description": string;
}

export interface IPxExtendedServiceOfferingCredentialSubject extends IPojoCredentialSubject {
    "@type": "PxExtendedServiceOfferingCredentialSubject";
    "gx:providedBy": INodeKindIRITypeId;
    "gx:aggregationOf": IGxDataResourceCredentialSubject[];
    "gx:termsAndConditions": IGxSOTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IGxDataAccountExport[];
    "gx:name": string;
    "gx:description": string;
    "px:assetId": string;
    "px:providerUrl": string;
    "@context": { [index: string]: string };
    type: string[];
}

export interface IPxExtendedServiceOfferingCredentialSubjectBuilder<C, B> extends IPojoCredentialSubjectBuilder<C, B> {
}

export interface IPxExtendedServiceOfferingCredentialSubjectBuilderImpl extends IPxExtendedServiceOfferingCredentialSubjectBuilder<IPxExtendedServiceOfferingCredentialSubject, IPxExtendedServiceOfferingCredentialSubjectBuilderImpl> {
    "gx:providedBy": INodeKindIRITypeId;
    "gx:aggregationOf": IGxDataResourceCredentialSubject[];
    "gx:termsAndConditions": IGxSOTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IGxDataAccountExport[];
    "gx:name": string;
    "gx:description": string;
    "px:assetId": string;
    "px:providerUrl": string;
}

export interface IPolicy {
    "@id": string;
    "odrl:permission": any[];
    "odrl:prohibition": any[];
    "odrl:obligation": any[];
    "odrl:target": IPolicyTarget;
    "@context": string;
    "@type": string;
}

export interface IPolicyTarget {
    "@id": string;
}

export interface HttpClient {

    request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; }): RestResponse<R>;
}

export class RestApplicationClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP POST /consumer/offer/accept
     * Java method: eu.possiblex.participantportal.application.boundary.ConsumerRestApiImpl.acceptContractOffer
     */
    acceptContractOffer(request: IConsumeOfferRequestTO): RestResponse<ITransferDetailsTO> {
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
     * HTTP GET /provider/id
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApiImpl.getParticipantId
     */
    getParticipantId(): RestResponse<IParticipantIdTO> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`provider/id` });
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

export type ITransferProcessState = "INITIAL" | "PROVISIONING" | "PROVISIONING_REQUESTED" | "PROVISIONED" | "REQUESTING" | "REQUESTED" | "STARTING" | "STARTED" | "SUSPENDING" | "SUSPENDED" | "COMPLETING" | "COMPLETED" | "TERMINATING" | "TERMINATED" | "DEPROVISIONING" | "DEPROVISIONING_REQUESTED" | "DEPROVISIONED";

export type IPojoCredentialSubjectUnion = IGxDataResourceCredentialSubject | IGxServiceOfferingCredentialSubject;

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
