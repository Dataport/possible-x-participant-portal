/* tslint:disable */
/* eslint-disable */

export interface IConsumerRestApi {
}

export interface IConsumeOfferRequestTO {
    counterPartyAddress: string;
    offerId: string;
}

export interface ICreateOfferRequestTO {
    offerType: string;
    offerName: string;
    offerDescription: string;
    fileName: string;
    policy: any;
}

export interface IExceptionTO {
    httpStatusCode: number;
    message: string;
}

export interface IOfferDetailsTO {
    offerId: string;
    name: string;
    description: string;
    contenttype: string;
    version: string;
    policies: IPolicy[];
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
     * HTTP POST /provider/offer
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApi.createOffer
     */
    createOffer(assetRequest: ICreateOfferRequestTO): RestResponse<any> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`provider/offer`, data: assetRequest });
    }
}

export type RestResponse<R> = Promise<R>;

export type ITransferProcessState = "INITIAL" | "PROVISIONING" | "PROVISIONED" | "REQUESTING" | "REQUESTED" | "STARTING" | "STARTED" | "COMPLETED";

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
