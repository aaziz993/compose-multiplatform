#!/bin/bash

. ./util.sh
. ./gpg/gpg-util.sh

gpg --gen-key --batch << EOF
%echo Generating GPG key
Key-Type:$1
Key-Length:$2
Subkey-Type:$3
Subkey-Length:$4
Name-Real:$5
Name-Comment:$6
Name-Email:$7
Expire-Date:$8
Passphrase:$9
%commit
%echo done
EOF

print_gpg
