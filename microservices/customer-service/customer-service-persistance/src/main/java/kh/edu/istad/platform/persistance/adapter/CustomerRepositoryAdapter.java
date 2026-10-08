package kh.edu.istad.platform.persistance.adapter;

import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import kh.edu.istad.platform.persistance.enity.CustomerEntity;
import kh.edu.istad.platform.persistance.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        customerJpaRepository.save(new CustomerEntity());
        return null;
    }
}
