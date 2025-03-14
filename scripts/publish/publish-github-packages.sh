#!/bin/bash

echo Publish to GitHub Packages

./gradlew :kmp-lib:publishAllPublicationsToGithubPackagesRepository
./gradlew :cmp-lib:publishAllPublicationsToGithubPackagesRepository
