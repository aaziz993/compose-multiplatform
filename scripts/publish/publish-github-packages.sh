#!/bin/bash

echo Publish to GitHub Packages

./gradlew :kmp-lib:publishAllPublicationsToGithubPackagesRepository :cmp-lib:publishAllPublicationsToGithubPackagesRepository
