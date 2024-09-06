import { ICredentialSubject } from "../views/offer/offer-data";

export function isGxServiceOfferingCs(cs: ICredentialSubject): boolean {
    return cs?.type === "gx:ServiceOffering";
}