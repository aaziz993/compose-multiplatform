#!/bin/bash

echo Publish to Maven Central

./gradlew :kmp-lib:publishAllPublicationsToMavenRepository :cmp-lib:publishAllPublicationsToMavenRepository
