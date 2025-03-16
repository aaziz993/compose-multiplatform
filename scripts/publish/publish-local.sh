#!/bin/bash

echo Publish to local

./gradlew publishAllPublicationsToBuildRepoRepository publishAllPublicationsToMavenLocalRepository
