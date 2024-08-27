/* tslint:disable */
/* eslint-disable */

export interface IProviderRestApi {
}

export interface IConsumeOfferRequestTO {
    counterPartyAddress: string;
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

export interface HttpClient {

    request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; }): RestResponse<R>;
}

export class RestApplicationClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP POST /consumer/acceptContractOffer
     * Java method: eu.possiblex.participantportal.application.boundary.ConsumerRestApi.acceptContractOffer
     */
    acceptContractOffer(request: IConsumeOfferRequestTO): RestResponse<any> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`consumer/acceptContractOffer`, data: request });
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

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
