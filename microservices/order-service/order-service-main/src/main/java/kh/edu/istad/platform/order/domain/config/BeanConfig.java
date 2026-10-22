package kh.edu.istad.platform.order.domain.config;

import kh.edu.istad.platform.order.service.OrderDomainService;
import kh.edu.istad.platform.order.service.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }

//    @Bean
//    public CustomerRepository customerRepository() {
//        return customerId -> Optional.of(new Customer(customerId));
//    }
//    @Bean
//    public BusinessRepository businessRepository() {
//        return business -> Optional.of(Business.builder()
//                .businessId(business.getId())
//                .active(true)
//                .products(business.getProducts())
//                .build());
//    }

}
