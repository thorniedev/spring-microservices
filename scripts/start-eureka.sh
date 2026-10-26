#!/usr/bin/env bash
export JAVA_HOME=${JAVA_HOME:-/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home}
export PATH="$JAVA_HOME/bin:$PATH"

echo "Starting Eureka Server on http://localhost:8761 ..."
./gradlew -p spring-cloud :eureka-server:run
