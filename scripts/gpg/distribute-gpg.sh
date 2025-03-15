#!/bin/bash

. ./gpg-util.sh

if ! is_gpg_key_in_keyserver keyserver.ubuntu.com "$gpg_key_id"; then
   gpg --keyserver keyserver.ubuntu.com --send-keys "$1"
fi

if ! is_gpg_key_in_keyserver keys.openpgp.org "$gpg_key_id"; then
   gpg --keyserver keys.openpgp.org --send-keys "$1"
fi

if ! is_gpg_key_in_keyserver pgp.mit.edu "$gpg_key_id"; then
   gpg --keyserver pgp.mit.edu --send-keys "$1"
fi
