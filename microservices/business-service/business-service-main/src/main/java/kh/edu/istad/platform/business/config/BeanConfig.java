package kh.edu.istad.platform.business.config;

import kh.edu.istad.platform.business.service.BusinessDomainService;
import kh.edu.istad.platform.business.service.BusinessDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public BusinessDomainService businessDomainService() {
        return new BusinessDomainServiceImpl();
    }
}