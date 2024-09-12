export const TBR_SERVICE_OFFERING_ID: string = "urn:uuid:GENERATED_SERVICE_OFFERING_ID";
export const TBR_DATA_RESOURCE_ID: string = "urn:uuid:GENERATED_DATA_RESOURCE_ID";

export interface IVerifiablePresentation {
    id: string;
    verifiableCredential: IVerifiableCredential[];
};

export interface IVerifiableCredential {
    credentialSubject: ICredentialSubject;
}

export interface ICredentialSubject {
    id: string;
    type: string;
    '@context': Map<string, string>
}

export interface IGxServiceOfferingCs extends ICredentialSubject {
    "gx:providedBy": INodeKindIRITypeId;
    "gx:termsAndConditions": IServiceOfferingTermsAndConditions[];
    "gx:policy": string[];
    "gx:dataProtectionRegime": string[];
    "gx:dataAccountExport": IDataAccountExport[];
    "gx:name": string;
    "gx:description": string;
}

export interface IGxDataResourceCs extends ICredentialSubject {
    "gx:copyrightOwnedBy": INodeKindIRITypeId;
    "gx:producedBy": INodeKindIRITypeId;
    "gx:exposedThrough": INodeKindIRITypeId;
    "gx:containsPII": boolean;
    "gx:obsoleteDateTime": string;
    "gx:expirationDateTime": string;
    "gx:policy": string[];
    "gx:license": string[];
    "gx:name": string;
    "gx:description": string;
}

export interface IServiceOfferingTermsAndConditions {
    'gx:URL': string;
    'gx:hash': string;
}

export interface IDataAccountExport {
    "gx:requestType": string;
    "gx:accessType": string;
    "gx:formatType": string;
}

export interface INodeKindIRITypeId {
    '@id': string;
}