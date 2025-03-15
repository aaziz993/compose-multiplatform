#!/bin/bash

echo Publish to Maven Central

./gradlew :kmp-lib:publishAllPublicationsToMavenLocalRepository :cmp-lib:publishAllPublicationsToMavenLocalRepository
