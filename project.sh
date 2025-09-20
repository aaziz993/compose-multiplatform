#!/usr/bin/env bash

. "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")/../scripts/log.sh"

function spotless_pre_commit(){
  local staged_files
  staged_files=$(git diff --staged --name-only)

  info "🧹 Formatting code in staged files…"
  ./gradlew spotlessApply
  for file in $staged_files; do
    if test -f "$file"; then
      git add "$file"
    fi
  done
}

function publish_local(){
  info "Publish to local"

  ./gradlew publishAllPublicationsToMavenLocalRepository publishAllPublicationsToBuildRepoRepository
}

function publish(){
  publish_local

  info "Publish"

  ./gradlew publishAllPublicationsToMavenLocalRepository publishAllPublicationsToBuildRepoRepository \
      publishAllPublicationsToGithubPackagesRepository publishAllPublicationsToSpacePackagesRepository \
      publishAllPublicationsToMavenRepository
}

_CLEAN=(
"*.podspec"
"*.gpg.*"
"*.keystore.*"
)

function clean(){
  set -euo pipefail

  ./gradlew clean

  IFS=$'\n\t'

  local find=()
  for f in "${_CLEAN[@]}"; do
      find+=(-name "$f" -o)
  done

  unset 'find[-1]'

  info "Clean files"
  find . -maxdepth 2 -type f \( "${find[@]}" \) -print -delete
}