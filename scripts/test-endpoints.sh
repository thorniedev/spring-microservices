#!/usr/bin/env bash

echo "=========================================================="
echo "TEST 1: Eureka Server Dashboard Check"
echo "=========================================================="
curl -s -o /dev/null -w "Eureka Dashboard HTTP Status: %{http_code}\n" http://localhost:8761/

echo ""
echo "=========================================================="
echo "TEST 2: Hardcoded Call (Order -> Customer direct :8081)"
echo "Route: GET http://localhost:8083/api/orders/test/hardcoded/1"
echo "=========================================================="
curl -i -X GET http://localhost:8083/api/orders/test/hardcoded/1

echo ""
echo "=========================================================="
echo "TEST 3: Eureka Discovery Call (Order -> CUSTOMER-SERVICE)"
echo "Route: GET http://localhost:8083/api/orders/test/eureka/1"
echo "=========================================================="
for i in {1..4}; do
  echo "--- Request #$i ---"
  curl -s http://localhost:8083/api/orders/test/eureka/1 | jq . || curl -s http://localhost:8083/api/orders/test/eureka/1
  echo ""
  sleep 1
done

echo ""
echo "=========================================================="
echo "TEST 4: Eureka Discovery Call (Order -> PAYMENT-SERVICE)"
echo "Route: GET http://localhost:8083/api/orders/test/eureka/payment-test"
echo "=========================================================="
curl -i -X GET http://localhost:8083/api/orders/test/eureka/payment-test
