#!/bin/bash

function gpg_long_key_id() {
    gpg --list-keys "$1" | sed -n 2p | xargs
}

function gpg_short_key_id() {
    gpg --list-keys --keyid-format short "$1" | awk '$1 == "pub" { print $2 }' | cut -d'/' -f2
}

function gpg_key() {
    gpg --pinentry-mode=loopback --passphrase="$1" --export-secret-keys --armor "$2" | grep -v '\-\-' | grep -v '^=.' | tr -d '\n'
}

function list_gpg_keys(){
    echo GPG short keys:"$(gpg_short_key_id "$1")"
    echo GPG long keys:"$(gpg_long_key_id "$1")"
    echo GPG keys:"$(gpg_key "$2" "$1")"
}

function is_gpg_key_in_keyserver() {
    if [[ "$(gpg --batch --keyserver "$1" --search-key "$2" 2>&1)" == *"not found on keyserver"* ]]; then
        false
    else
        true
    fi
}
