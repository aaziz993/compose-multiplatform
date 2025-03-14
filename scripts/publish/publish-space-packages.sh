#!/bin/bash

echo Publish to Space Packages

. scripts/export-gpg-key.sh

./gradlew :kmp-lib:publishAllPublicationsToSpacePackagesRepository
./gradlew :cmp-lib:publishAllPublicationsToSpacePackagesRepository
