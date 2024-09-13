/* tslint:disable */
/* eslint-disable */

export interface IConsumerRestApi {
}

export interface IProviderRestApi {
    participantId: IParticipantIdTO;
}

export interface IResourceShapeRestApi {
    gxDataResourceShape: string;
    gxPhysicalResourceShape: string;
    gxSoftwareResourceShape: string;
    gxVirtualResourceShape: string;
    gxInstantiatedVirtualResourceShape: string;
}

export interface IServiceOfferingShapeRestApi {
    gxServiceOfferingShape: string;
}

export interface IConsumeOfferRequestTO {
    counterPartyAddress: string;
    offerId: string;
}

export interface ICreateOfferRequestTO {
    credentialSubjectList: any[];
    fileName: string;
    policy: IPolicy;
}

export interface ICreateOfferResponseTO {
    edcResponseId: string;
    fhResponseId: string;
}

export interface IOfferDetailsTO {
    offerId: string;
    offerType: string;
    creationDate: Date;
    name: string;
    description: string;
    contentType: string;
}

export interface IParticipantIdTO {
    participantId: string;
}

export interface ISelectOfferRequestTO {
    counterPartyAddress: string;
    offerId: string;
}

export interface ITransferDetailsTO {
    state: ITransferProcessState;
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
     * HTTP POST /provider/offer
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApiImpl.createOffer
     */
    createOffer(createOfferRequestTO: ICreateOfferRequestTO): RestResponse<ICreateOfferResponseTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`provider/offer`, data: createOfferRequestTO });
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

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
