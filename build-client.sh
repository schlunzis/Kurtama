#!/bin/sh

set -e

os="$1" #TODO auto detect
version="$2"
windowsVersion="$3"
input="./client/target"
mainJar="client-${version}.jar"
resourceDir="./client/jpackage"
destination="./target/jpackage-out"
name="Kurtama"

if  [ "$os" = linux ]; then
  echo "Building for Linux"
  echo "Building client jar"
  ./mvnw --projects client --also-make --batch-mode --update-snapshots clean install package

  echo "Running jpackage"
  jpackage --type deb \
  --verbose \
  --input ${input} \
  --main-jar "${mainJar}" \
  --resource-dir ${resourceDir} \
  --name ${name} \
  --app-version "${version}" \
  --dest ${destination} \
  --linux-package-name "kurtama-client" \
  --linux-menu-group Game \
  --linux-app-category Game \
  --linux-shortcut


elif  [ "$os" = windows ]; then
  echo "Building for Windows"
  echo "Building client jar"
  # maven wrapper is a hassle here TODO
  ./mvnw --projects client --also-make --batch-mode --update-snapshots clean install package

  echo "Running jpackage"
  jpackage --type exe \
  --verbose \
  --input ${input} \
  --main-jar "${mainJar}" \
  --resource-dir ${resourceDir} \
  --name ${name} \
  --app-version "${windowsVersion}" \
  --dest ${destination} \
  --win-upgrade-uuid "54e9b129-e6a4-4272-bd94-13079eb6ae6d" \
  --win-menu


else
  echo "No OS given. Provide 'linux' or 'windows' as an argument."
fi

exit 0
