#!/usr/bin/env bash

. "$(dirname "$(readlink -f "$0")")/../../../scripts/gpg-utils.sh"

gpg_gen_key signing.gnupg. local.properties
