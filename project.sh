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

  ./gradlew publishAllPublicationsToGithubPackagesRepository publishAllPublicationsToMavenCentralRepository
}

_CLEAN=(
".*.toml"
"*.podspec"
"*.yaml.cache"
".*.properties"
".*.kt"
".*.html"
"*-png.png"
"*-ico.ico"
"*-icns.icns"
".*.gpg"
".*.p12"
".*.pkcs12"
".*.jks"
)

function clean() {
    set -euo pipefail

    ./gradlew clean

    info "🧹Cleaning files..."

    local find_args=()
    for f in "${_CLEAN[@]}"; do
        find_args+=(-name "$f" -o)
    done

    unset "find_args[${#find_args[@]}-1]"

    find . -maxdepth 2 -type f \( "${find_args[@]}" \) -print -delete
}
