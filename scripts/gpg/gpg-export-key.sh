#!/usr/bin/env bash

. "$(dirname "$(readlink -f "$0")")/../../../scripts/gpg-utils.sh"

gpg_export_key
