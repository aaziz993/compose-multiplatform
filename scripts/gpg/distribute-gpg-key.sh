#!/bin/bash

. gpg-util.sh

if ! is_gpg_key_in_keyserver keyserver.ubuntu.com "$1"; then
   gpg --keyserver keyserver.ubuntu.com --send-keys "$1"
fi

if ! is_gpg_key_in_keyserver keys.openpgp.org "$1"; then
   gpg --keyserver keys.openpgp.org --send-keys "$1"
fi

if ! is_gpg_key_in_keyserver pgp.mit.edu "$1"; then
   gpg --keyserver pgp.mit.edu --send-keys "$1"
fi
