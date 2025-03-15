#!/bin/bash

gpg --batch --yes --delete-secret-keys $(gpg --list-secret-keys "$1" --with-colons | grep ^fpr | cut -d: -f10) &&
gpg --batch --yes --delete-keys $(gpg --list-keys "$1" --with-colons | grep ^pub | cut -d: -f5)
