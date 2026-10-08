package kh.edu.istad.platform.customer.bean;

import kh.edu.istad.platform.customer.service.CustomerDomainService;
import kh.edu.istad.platform.customer.service.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CustomerDomainService customerDomainService(){
        return new CustomerDomainServiceImpl();
    }

}
