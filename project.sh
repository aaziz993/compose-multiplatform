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
  info "🧹Cleaning apple-app..."

  pushd "apple-app" >/dev/null || return
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
  "*/app-icon-png.png"
  "*/app-icon-ico.ico"
  "*/app-icon-icns.icns"
  "apple-app/ios-app/Assets.xcassets/AppIcon.appiconset/*.png"
  "apple-app/tvos-app/Assets.xcassets/App Icon & Top Shelf Image.brandassets/*/*.png"
  "apple-app/watchos-app/Assets.xcassets/AppIcon.appiconset/*.png"
  "compose_app/*/favicon*.svg"
)

function clean_files(){
  info "🧹Cleaning files..."

  for pattern in "${_CLEAN_PATH[@]}"; do
    find . -type f -path "./$pattern" -print -delete
  done
}

function clean() {
  set -e

  clean_apple_app

  info "🧹Cleaning project..."

  ./gradlew "clean"

  rm -rf "build"
  rm -rf "*/build"

  clean_files
}

clean_mac() {
  info "🧹 Starting cleanup..."

  # 1. Empty system and user caches
  info "Clearing user cache..."
  rm -rf ~/Library/Caches/*

  info "Clearing system cache (requires sudo)..."
  sudo rm -rf /Library/Caches/* /System/Library/Caches/*

  # 2. Delete old logs
  info "Clearing log files..."
  rm -rf ~/Library/Logs/*
  sudo rm -rf /private/var/log/*

  # 3. Xcode Derived Data, Archives, Module Cache
  info "Cleaning Xcode derived data..."
  rm -rf ~/Library/Developer/Xcode/DerivedData/*
  rm -rf ~/Library/Developer/Xcode/Archives/*
  rm -rf ~/Library/Developer/Xcode/DerivedData/*

  info "Cleaning Xcode module cache..."
  rm -rf ~/Library/Developer/Xcode/DerivedData/ModuleCache.noindex

  # 4. CocoaPods cache & build artifacts
  info "Cleaning CocoaPods caches..."
  pod cache clean --all 2>/dev/null || true
  rm -rf ~/Library/Caches/CocoaPods

  # 6. Temporary files & /tmp
  info "Clearing temporary files..."
  sudo rm -rf /private/var/tmp/*
  rm -rf /tmp/*

  # 7. Trash
  info "Emptying Trash..."
  rm -rf ~/.Trash/* /Volumes/*/.Trashes/*

  df -h /
}
