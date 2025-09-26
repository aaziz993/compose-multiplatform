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
    rm -rf "*.xcworkspace"
    rm -rf "*.xcodeproj/project.xcworkspace"
    rm -rf "*.xcodeproj/xcuserdata"
    rm -rf "Podfile.lock"
  popd >/dev/null
}

_CLEAN_PATH=(
  "gradle/.*.toml"
  "license.header.txt"
  "license.header.properties"
  "license.header.kt"
  "license.header.html"
  "klib/signing.gpg"
  "clib/signing.gpg"
  "shared/signing.gpg"
  "compose-app/signing.android.*.pkcs12"
  "compose-app/compose_app.podspec"
  "*/composeResources/*/app-icon-png.png"
  "*/composeResources/*/app-icon-ico.ico"
  "*/composeResources/*/app-icon-icns.icns"
  "appleApp/iosApp/Assets.xcassets/AppIcon.appiconset/*.png"
  "appleApp/TVosApp/Assets.xcassets/App Icon & Top Shelf Image.brandassets/*/*.png"
  "appleApp/WatchosApp Watch App/Assets.xcassets/AppIcon.appiconset/*.png")

function clean_files(){
  info "🧹Cleaning files..."

  for pattern in "${_CLEAN_PATH[@]}"; do
    find . -type f -path "./$pattern" -print -delete
  done
}

function clean() {
  set -euo pipefail

  clean_apple_app

  info "🧹Cleaning project..."

  ./gradlew "clean"

  rm -rf "build"
  rm -rf "*/build"

  clean_files
}
