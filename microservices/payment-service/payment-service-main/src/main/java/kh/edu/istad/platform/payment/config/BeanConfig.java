package kh.edu.istad.platform.payment.config;

import kh.edu.istad.platform.payment.service.PaymentDomainService;
import kh.edu.istad.platform.payment.service.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}