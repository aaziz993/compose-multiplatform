#!/bin/bash

gpg --list-secret-keys "$1" --with-colons | grep ^fpr | cut -d: -f10 | xargs gpg --batch --yes --delete-secret-keys &&
gpg --list-keys "$1" --with-colons | grep ^pub | cut -d: -f5 | xargs gpg --batch --yes --delete-keys
