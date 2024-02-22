#!/bin/sh

set -e

version="0.0.1-SNAPSHOT"

echo "Building client jar"
./mvnw --projects client --also-make --batch-mode --update-snapshots clean install package

echo "Running jpackage"
jpackage --type deb \
--input ./client/target \
--main-jar ./client/target/client-${version}.jar \
--resource-dir ./client/jpackage \
--name kurtama-client \
--app-version ${version} \
--linux-package-name kurtama-client \
--linux-menu-group Game \
--linux-app-category Game \
--linux-app-release ${version} \
--linux-shortcut \
--main-class ClientLauncher \
--verbose \
--dest ./target/jpackage-out

exit 0
