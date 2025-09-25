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

function clean_apple_app(){
  info "🧹Cleaning appleApp..."

  pushd "appleApp" >/dev/null || return
    pod deintegrate
    pod cache clean --all
    rm -rf appleApp.xcworkspace
    rm -rf appleApp.xcodeproj/project.xcworkspace
    rm -rf appleApp.xcodeproj/xcuserdata
    rm -rf Podfile.lock
  popd >/dev/null
}

_CLEAN=(
"*.yaml.db"
".*.toml"
"*.podspec"
"*.yaml.cache"
"license.header.txt"
"license.header.properties"
"license.header.kt"
"license.header.html"
"*-png.png"
"*-ico.ico"
"*-icns.icns"
".*.gpg"
".*.p12"
".*.pkcs12"
".*.jks"
)

function clean_files(){
  info "🧹Cleaning files..."

  local find_args=()
  for arg in "${_CLEAN[@]}"; do
    find_args+=(-name "$arg" -o)
  done

  unset "find_args[${#find_args[@]}-1]"

  find . -maxdepth 2 -type f \( "${find_args[@]}" \) -print -delete
}

function clean() {
    set -euo pipefail

    clean_apple_app

    clean_files

    info "🧹Cleaning project..."

    rm -rf ".idea"
    ./gradlew "clean"
    rm -rf ".gradle"
    rm -rf "build"
    rm -rf "*/build"
}
