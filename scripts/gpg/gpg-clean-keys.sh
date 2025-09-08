#!/usr/bin/env bash

set -euo pipefail

. "$(dirname "$(readlink -f "$0")")/../../../scripts/gpg-utils.sh"

gpg_clean_keys "$@"
