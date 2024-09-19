import { IPojoCredentialSubject } from "../services/mgmt/api/backend";

export function isGxServiceOfferingCs(cs: IPojoCredentialSubject): boolean {
    return cs?.["@type"] === "gx:ServiceOffering";
}

export function isDataResourceCs(cs: IPojoCredentialSubject): boolean {
    return cs?.["@type"] === "gx:DataResource";
}