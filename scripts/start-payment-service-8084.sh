#!/usr/bin/env bash
export JAVA_HOME=${JAVA_HOME:-/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home}
export PATH="$JAVA_HOME/bin:$PATH"

echo "Starting Payment Service on port 8084 ..."
./gradlew :microservices:payment-service:payment-service-main:bootRun --args='--server.port=8084'
