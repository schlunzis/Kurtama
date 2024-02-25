#!/bin/sh

set -e

version="0.0.1-SNAPSHOT"
input="./client/target"
main-jar="./client/target/client-${version}.jar"
resource-dir="./client/jpackage"
main-class="ClientLauncher"
destination="./target/jpackage-out"
name="kurtama-client"

if  [ "$1" = linux ]; then
  echo "Building for Linux"
  echo "Building client jar"
  ./mvnw --projects client --also-make --batch-mode --update-snapshots clean install package

  echo "Running jpackage"
  jpackage --type deb \
  --input ${input} \
  --main-jar ${main-jar} \
  --resource-dir ${resource-dir} \
  --name ${name} \
  --app-version ${version} \
  --main-class ${main-class} \
  --dest ${destination} \
  --linux-package-name kurtama-client \
  --linux-menu-group Game \
  --linux-app-category Game \
  --linux-app-release ${version} \
  --linux-shortcut
elif  [ "$1" = windows ]; then
  echo "Building for Windows"
  echo "Building client jar"
  ./mvn.cmd --projects client --also-make --batch-mode --update-snapshots clean install package

  echo "Running jpackage"
  jpackage --type exe \
  --input ${input} \
  --main-jar ${main-jar} \
  --resource-dir ${resource-dir} \
  --name ${name} \
  --app-version ${version} \
  --main-class ${main-class} \
  --dest ${destination} \
  --win-upgrade-uuid "54e9b129-e6a4-4272-bd94-13079eb6ae6d" \
  --win-menu
else
  echo "No OS given. Provide 'linux' or 'windows' as an argument."
fi

exit 0
