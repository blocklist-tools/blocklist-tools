#!/bin/sh
set -e

echo "${GPG_PASSPHRASE}" | gpg --import /run/secrets/gpg-key.asc
echo "43CB23B0D79C3B8E92E610174940B41048AF73EA:6" | gpg --import-ownertrust

/usr/local/bin/mvn-entrypoint.sh mvn "$@"
