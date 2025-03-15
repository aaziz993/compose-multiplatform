#!/bin/bash

echo Publish to Space Packages

. scripts/export-gpg-key.sh

./gradlew :kmp-lib:publishAllPublicationsToSpacePackagesRepository :cmp-lib:publishAllPublicationsToSpacePackagesRepository
