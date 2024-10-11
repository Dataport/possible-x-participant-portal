#! /bin/bash

BASEDIR=$(dirname $0)
HELP_TEXT="
This is a small script to help with deployment.
This script will create a namespace (if it does not already exist), 
create and install a sealed secret from a local secret file if you provide the parameter
and deploy the helm chart.

The script has some parameters:

-n, --namespace - the namespace where the management console should be deployed to
-g, --github-auth-secret - (optional) the path to the secret that is used to retrieve the data
-f, --value-file - the path to the value file for the helm deployment
-h, --help - prints this message
-u, --upgrade - upgrade the helm installation instead of installing it
-e, --environment - the environment to deploy, e.g. dev/demo
"
NAMESPACE=
DELETE_NAMESPACE=
GITHUB_AUTH_SECRET=
HELM_OPERATION=install
while [[ "$#" -gt 0 ]]; do
  case "${1}" in
    (-n | --namespace)
      NAMESPACE=${2}
      shift 2
    ;;
    (-g | --github-auth-secret)
      GITHUB_AUTH_SECRET=${2}
      shift 2
    ;;
    (-f | --value-file)
      VALUE_FILE=${2}
      shift 2
    ;;
    (-e | --environment)
      ENVIRONMENT=${2}
      shift 2
    ;;
    (-h | --help)
      echo "${HELP_TEXT}"
      exit 0
    ;;
    (-u | --upgrade)
      HELM_OPERATION=upgrade
      shift
    ;;
    (*)
        echo "Unknown parameter '${1}'"
        exit 1    # error
    ;;
  esac
done


if [ -z "$NAMESPACE" ]; then
  echo "Error: please specify a namespace"
  exit 1
fi

if [ -z "$VALUE_FILE" ]; then
  echo "Error: please specify a value file"
  exit 1
fi
kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -
GITHUB_SEALED_SECRET_FILE="$BASEDIR/$ENVIRONMENT/sealed-secrets/$NAMESPACE-github-auth-sealed-secret.yaml"
if [ -n "$GITHUB_AUTH_SECRET" ] && ! [ -f "$GITHUB_SEALED_SECRET_FILE" ]; then
  kubeseal -f "$GITHUB_AUTH_SECRET" -w "$GITHUB_SEALED_SECRET_FILE" -n "$NAMESPACE"
  kubectl apply -f "$GITHUB_SEALED_SECRET_FILE"
fi

helm $HELM_OPERATION -n "$NAMESPACE" edc-management "$BASEDIR/helm" -f "$VALUE_FILE"