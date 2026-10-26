package kh.edu.istad.platform.order.domain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/orders/test")
public class InterServiceTestController {

    private final RestTemplate restTemplate;
    private final RestTemplate loadBalancedRestTemplate;

    public InterServiceTestController(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("loadBalancedRestTemplate") RestTemplate loadBalancedRestTemplate
    ) {
        this.restTemplate = restTemplate;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
    }

    // ==========================================================
    // PART 1: Hard-coded Inter-Service REST Call
    // Direct call using static IP/port: http://localhost:8081/api/customers/{id}
    // ==========================================================
    @GetMapping("/hardcoded/{customerId}")
    public Object getCustomerHardcoded(@PathVariable String customerId) {
        String url = "http://localhost:8081/api/customers/" + customerId;
        log.info("Executing HARDCODED call to: {}", url);
        return restTemplate.getForObject(url, Object.class);
    }

    // ==========================================================
    // PART 3: Eureka-based Dynamic Discovery & Load Balanced Calls
    // Service-name resolution: http://customer-service/api/customers/{id}
    // ==========================================================
    @GetMapping("/eureka/{customerId}")
    public Object getCustomerEureka(@PathVariable String customerId) {
        String url = "http://customer-service/api/customers/" + customerId;
        log.info("Executing EUREKA discovery call to: {}", url);
        return loadBalancedRestTemplate.getForObject(url, Object.class);
    }

    @GetMapping("/eureka/payment-test")
    public Object testPaymentEureka() {
        String url = "http://payment-service/api/payments";
        log.info("Executing EUREKA call to payment-service: {}", url);

        Map<String, Object> paymentRequest = Map.of(
                "orderId", UUID.randomUUID().toString(),
                "customerId", UUID.randomUUID().toString(),
                "price", new BigDecimal("99.99")
        );

        return loadBalancedRestTemplate.postForObject(url, paymentRequest, Object.class);
    }
}
