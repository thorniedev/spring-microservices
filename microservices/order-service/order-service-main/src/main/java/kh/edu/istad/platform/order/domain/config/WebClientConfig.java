package kh.edu.istad.platform.order.domain.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Standard RestTemplate for hardcoded calls (direct IP/port)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // LoadBalanced RestTemplate for Eureka service-name resolution (e.g. http://customer-service)
    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    // Standard WebClient Builder
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // LoadBalanced WebClient Builder
    @LoadBalanced
    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
