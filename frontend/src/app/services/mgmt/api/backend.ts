/* tslint:disable */
/* eslint-disable */

export interface IConsumerRestApi {
}

export interface IProviderRestApi {
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
    offerType: string;
    creationDate: Date;
    name: string;
    description: string;
    contentType: string;
}

export interface ISelectOfferRequestTO {
    counterPartyAddress: string;
    offerId: string;
}

export interface ITransferDetailsTO {
    state: ITransferProcessState;
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
     * Java method: eu.possiblex.participantportal.application.boundary.ProviderRestApiImpl.createOffer
     */
    createOffer(createOfferRequestTO: ICreateOfferRequestTO): RestResponse<any> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`provider/offer`, data: createOfferRequestTO });
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
