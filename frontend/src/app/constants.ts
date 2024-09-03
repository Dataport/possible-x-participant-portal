export const POLICY_MAP: { [key: string]: any } = {
  'Everything is allowed': {
    "policy": {
      "@type": "odrl:Set",
      "odrl:permission": [
        {
          "odrl:action": {
            "odrl:type": "http://www.w3.org/ns/odrl/2/use"
          }
        },
        {
          "odrl:action": {
            "odrl:type": "http://www.w3.org/ns/odrl/2/transfer"
          }
        }
      ],
      "odrl:prohibition": [],
      "odrl:obligation": []
    }
  }
};
