#!/bin/sh

set -e

version="0.0.1-SNAPSHOT"
input="./client/target"
mainJar="client-${version}.jar"
resourceDir="./client/jpackage"
mainClass="ClientLauncher"
destination="./target/jpackage-out"
name="kurtama-client"

if  [ "$1" = linux ]; then
  echo "Building for Linux"
  echo "Building client jar"
  ./mvnw --projects client --also-make --batch-mode --update-snapshots clean install package

  echo "Running jpackage"
  jpackage --type deb \
  --verbose \
  --input ${input} \
  --main-jar ${mainJar} \
  --resource-dir ${resourceDir} \
  --name ${name} \
  --app-version ${version} \
  --main-class ${mainClass} \
  --dest ${destination} \
  --linux-package-name kurtama-client \
  --linux-menu-group Game \
  --linux-app-category Game \
  --linux-shortcut
elif  [ "$1" = windows ]; then
  echo "Building for Windows"
  echo "Building client jar"
  cmd.exe ./mvn.cmd --projects client --also-make --batch-mode --update-snapshots clean install package
  ls -al ./client

  echo "Running jpackage"
  jpackage --type exe \
  --verbose \
  --input ${input} \
  --main-jar ${mainJar} \
  --resource-dir ${resourceDir} \
  --name ${name} \
  --app-version ${version} \
  --main-class ${mainClass} \
  --dest ${destination} \
  --win-upgrade-uuid "54e9b129-e6a4-4272-bd94-13079eb6ae6d" \
  --win-menu
else
  echo "No OS given. Provide 'linux' or 'windows' as an argument."
fi

exit 0
