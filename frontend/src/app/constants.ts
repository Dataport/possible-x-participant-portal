export const POLICY_MAP: { [key: string]: any } = {
    'Everything is allowed': {
        "policy": {
          "@id": "GENERATED_POLICY_ID",
          "@type": "odrl:Set",
          "odrl:permission": [
            {
              "odrl:target": "GENERATED_ASSET_ID",
              "odrl:action": {
                "odrl:type": "http://www.w3.org/ns/odrl/2/use"
              }
            },
            {
              "odrl:target": "GENERATED_ASSET_ID",
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