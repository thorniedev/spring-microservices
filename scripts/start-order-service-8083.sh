#!/usr/bin/env bash
export JAVA_HOME=${JAVA_HOME:-/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home}
export PATH="$JAVA_HOME/bin:$PATH"

echo "Starting Order Service on port 8083 ..."
./gradlew :microservices:order-service:order-service-main:bootRun --args='--server.port=8083'
