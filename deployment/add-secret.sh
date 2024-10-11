#! /bin/bash

BASEDIR=$(dirname $0)
HELP_TEXT="
This is a small script to help with deployment.
This script will create a namespace (if it does not already exist), 
create and install a sealed secret from a local secret file if you provide the parameter
and deploy the helm chart.

The script has some parameters:

-n, --namespace - the namespace where the management console should be deployed to
-k, --key - the key to add
-v, --value - the value to add
-h, --help - prints this message
-t, --target - the instance for with to upgrade the key/value (either provider, consumer or all)
-e, --environment - the environment to install to (dev/demo)
"
TARGET=all
KEY=
VALUE=
ENVIRONMENT=
while [[ "$#" -gt 0 ]]; do
  case "${1}" in
    (-t | --target)
      TARGET=${2}
      shift 2
    ;;
    (-k | --key)
      KEY=${2}
      shift 2
    ;;
    (-v | --value)
      VALUE=${2}
      shift 2
    ;;
    (-e | --environment)
      ENVIRONMENT=${2}
      shift 2
    ;;
    (-n | --namespace)
      NAMESPACE=${2}
      shift 2
    ;;
    (-h | --help)
      echo "${HELP_TEXT}"
      exit 0
    ;;
    (*)
        echo "Unknown parameter '${1}'"
        exit 1    # error
    ;;
  esac
done


if [ -z "$KEY" ]; then
  echo "Error: please specify a key"
  exit 1
fi

if [ -z "$VALUE" ]; then
  echo "Error: please specify a value"
  exit 1
fi

if [ -z "$ENVIRONMENT" ]; then
  echo "Error: please specify an environment"
  exit 1
fi

function add_secret() {
  SECRET_FILE="$BASEDIR/$ENVIRONMENT/secrets/$1-management-application-secret.yaml"
  SEALED_SECRET_FILE="$BASEDIR/$ENVIRONMENT/sealed-secrets/$1-management-application-secret.yaml"
  KEY=$2
  VALUE=$3
  NAMESPACE=$5
  TMP_FILE=$(mktemp)
  kubectl patch --dry-run=client -p '{"data":{"'"$KEY"'": "'"$(echo -n "$VALUE" | base64)"'"}}' -f $SECRET_FILE -o yaml > $TMP_FILE
  cp $TMP_FILE $SECRET_FILE
  rm $TMP_FILE
  kubeseal -f "$SECRET_FILE" -w "$SEALED_SECRET_FILE" -n "$NAMESPACE"
}

if [ "$TARGET" = "provider" ] || [ "$TARGET" = "all" ]; then
  add_secret "provider" "$KEY" "$VALUE" "$NAMESPACE"
fi

if [ "$TARGET" = "consumer" ] || [ "$TARGET" = "all" ]; then
  add_secret "consumer" "$KEY" "$VALUE" "$NAMESPACE"
fi