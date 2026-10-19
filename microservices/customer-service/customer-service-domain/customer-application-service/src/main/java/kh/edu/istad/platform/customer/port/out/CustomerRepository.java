package kh.edu.istad.platform.customer.port.out;

import kh.edu.istad.platform.customer.entity.Customer;
import reactor.core.publisher.Mono;

// Domain Repository Pattern (DDD), not JAP repository
public interface CustomerRepository
{
    Customer save(Customer customer);

    Mono<Customer> save2(Customer customer);
}
