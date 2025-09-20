#!/usr/bin/env bash

set -euo pipefail

./gradlew clean

IFS=$'\n\t'

PATTERNS=(
    ".signing*"
    "*.podspec"
)

FIND_ARGS=()
for pat in "${PATTERNS[@]}"; do
    FIND_ARGS+=(-name "$pat" -o)
done

unset 'FIND_ARGS[-1]'

echo "Clean files"
find . -maxdepth 2 -type f \( "${FIND_ARGS[@]}" \) -print -delete
