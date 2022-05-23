#!/bin/bash

set -ex

ng build --prod
docker build -t ghcr.io/musikfreunde/leocode-frontend:latest .
docker image push ghcr.io/musikfreunde/leocode-frontend:latest
