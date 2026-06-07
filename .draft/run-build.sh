#!/bin/bash
# Build helper. Selects the JDK via cli-assistant (NOT a hardcoded JAVA_HOME).
# Usage: ./.draft/run-build.sh <jdk-version> [gradle-args...]
#   e.g. ./.draft/run-build.sh 11 clean build
#        ./.draft/run-build.sh 17 clean build
#
# IMPORTANT: `cli-assistant env java use` is a profile shell-FUNCTION that eval's
# `export JAVA_HOME/PATH` into the CURRENT shell. It only works if it runs in the
# same shell as the build and is NOT piped/redirected into a subshell. This script
# keeps `use` and gradle in one shell, so do not pipe the `use` line.
set -e
JDK="${1:?usage: run-build.sh <jdk-version> [gradle-args...]}"
shift
cli-assistant env java use "$JDK"
java -version
./gradlew "${@:-clean build}" --console=plain
