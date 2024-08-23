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
    edcOffering: IDcatDataset;
}

export interface ISelectOfferRequestTO {
    counterPartyAddress: string;
    offerId: string;
}

export interface ITransferDetailsTO {
    state: string;
}

export interface IDcatDataset {
    version: string;
    name: string;
    description: string;
    contenttype: string;
    "@id": string;
    "@type": string;
    "odrl:hasPolicy": IPolicy[];
    "dcat:distribution": IDcatDistribution[];
    id: string;
}

export interface IPolicy {
    "@id": string;
    "odrl:permission": string[];
    "odrl:prohibition": string[];
    "odrl:obligation": string[];
    "odrl:target": IPolicyTarget;
    "@type": string;
}

export interface IDcatDistribution {
    "@type": string;
    "dct:format": { [index: string]: string };
    "dcat:accessService": string;
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
     * HTTP POST /consumer/acceptContractOffer
     * Java method: eu.possiblex.participantportal.application.boundary.ConsumerRestApi.acceptContractOffer
     */
    acceptContractOffer(request: IConsumeOfferRequestTO): RestResponse<ITransferDetailsTO> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`consumer/offer/accept`, data: request });
    }

    /**
     * HTTP POST /consumer/offer/select
     * Java method: eu.possible_x.backend.application.boundary.ConsumerRestApiImpl.selectContractOffer
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

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
