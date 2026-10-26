#!/usr/bin/env bash
export JAVA_HOME=${JAVA_HOME:-/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home}
export PATH="$JAVA_HOME/bin:$PATH"

echo "Starting Customer Service (Instance 1) on port 8081 ..."
./gradlew :microservices:customer-service:customer-service-main:bootRun --args='--server.port=8081'
