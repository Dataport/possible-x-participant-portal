#!/bin/sh

#  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


BASEDIR=$(dirname $0)
HELP_TEXT="
This is a small script to help with deployment.
This script will create a namespace (if it does not already exist), 
create and install a sealed secret from a local secret file if you provide the parameter
and deploy the helm chart.

The script has some parameters:

-n, --namespace - the namespace where the management console should be deployed to
-f, --env-file - the env file path that will be made into a secret
-t, --target - the path of the sealed secret you want to create
-h, --help - prints this message
-s, --secret-name - the name of the secret that can be referenced in k8s
"
while [[ "$#" -gt 0 ]]; do
  case "${1}" in
    (-f | --env-file)
      FILE_PATH=${2}
      shift 2
    ;;
    (-n | --namespace)
      NAMESPACE=${2}
      shift 2
    ;;
    (-s | --secret-name)
      SECRET_NAME=${2}
      shift 2
    ;;
    (-t | --target)
      SEALED_SECRET_PATH=${2}
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

if [ -z "$FILE_PATH" ]; then
  echo "Error: please specify the path of the env file that has to be sealed"
  exit 1
fi
if [ -z "$SEALED_SECRET_PATH" ]; then
  echo "Error: please specify the path of the sealed secret as target"
  exit 1
fi

if [ -z "$SECRET_NAME" ]; then
  echo "Error: please specify the name of the secret"
  exit 1
fi

if [ -z "$NAMESPACE" ]; then
  echo "Error: please specify a namespace"
  exit 1
fi



kubectl create secret generic $SECRET_NAME --type Opaque --dry-run=client --namespace $NAMESPACE --output yaml --from-env-file $FILE_PATH |\
kubeseal --namespace $NAMESPACE --format yaml --sealed-secret-file $SEALED_SECRET_PATH