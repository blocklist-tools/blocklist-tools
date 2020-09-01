#!/bin/bash

set -e

if [[ ! -f '/root/.m2/settings.xml' ]]; then
    cp '/opt/blocklist-tools/settings.xml' '/root/.m2/settings.xml'
fi

if [[ "$@" =~ 'deploy' ]]
then
    apt-get update
    apt-get install git gnupg2 -y
    git config --global user.email 'daniel@developerdan.com'
    git config --global user.name 'Daniel'

    mkdir -p ~/.gnupg
    chmod 700 ~/.gnupg
    echo "use-agent" >> ~/.gnupg/gpg.conf
    echo "pinentry-mode loopback"  >> ~/.gnupg/gpg.conf
    echo "allow-loopback-pinentry" >> ~/.gnupg/gpg-agent.conf
    echo RELOADAGENT | gpg-connect-agent

    echo "${GPG_PASSPHRASE}" | gpg --import /run/secrets/gpg-key.asc
    echo '43CB23B0D79C3B8E92E610174940B41048AF73EA:6' | gpg --import-ownertrust
fi

/usr/local/bin/mvn-entrypoint.sh mvn "$@"
