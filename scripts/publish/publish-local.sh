#!/bin/bash

echo Publish to local

./gradlew publishAllPublicationsToMavenLocalRepository publishAllPublicationsToBuildRepoRepository
