#!/bin/bash

echo Publish to Maven Central

./gradlew :kmp-lib:publishAllPublicationsToMavenRepository
./gradlew :cmp-lib:publishAllPublicationsToMavenRepository
