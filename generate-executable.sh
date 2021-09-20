#!/bin/bash

# Fail on error
set -e

echo "Build nicokosi/hubstats JAR file:"
lein uberjar

echo "Install GraalVM via SDKMAN!:"
curl --silent "https://get.sdkman.io" | bash || echo 'SDKMAN! already installed'
source "$HOME/.sdkman/bin/sdkman-init.sh"
GRAALVM_VERSION=21.2.0.r16-grl
sdkman_auto_answer=true sdk install java $GRAALVM_VERSION > /dev/null
sdk use java $GRAALVM_VERSION

echo "Build executable from JAR via GraalVM:"
gu install native-image && \
native-image \
    --allow-incomplete-classpath \
    --auto-fallback \
    --initialize-at-run-time=sun.awt.dnd.SunDropTargetContextPeer$EventDispatcher \
    --no-server \
    --report-unsupported-elements-at-runtime \
    -Dclojure.compiler.direct-linking=true \
    -H:+ReportExceptionStackTraces \
    -H:ConfigurationFileDirectories=conf \
    -jar ./target/uberjar/strava-activity-graphs-0.1.0-SNAPSHOT-standalone.jar \
    strava-activity-graphs.core

echo "Executable has been built! ✅

It can be run this way:

 $ # Replace \$STRAVA_TOKEN with your Strava access token)
 $ ./strava-activity-graphs.core \$STRAVA_TOKEN"