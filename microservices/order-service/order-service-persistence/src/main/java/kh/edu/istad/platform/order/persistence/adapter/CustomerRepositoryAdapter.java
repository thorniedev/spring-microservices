package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.entity.Customer;
import kh.edu.istad.platform.order.port.out.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {
    @Override
    public Optional<Customer> findCustomer(CustomerId customerId) {
        return Optional.of(new Customer(customerId));
    }
}
