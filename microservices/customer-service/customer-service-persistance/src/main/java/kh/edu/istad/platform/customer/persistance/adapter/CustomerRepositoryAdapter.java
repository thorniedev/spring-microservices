package kh.edu.istad.platform.customer.persistance.adapter;

import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import kh.edu.istad.platform.customer.persistance.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistance.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = customerPersistenceMapper.toEntity(customer);
        CustomerEntity saveEntity = customerJpaRepository.save(customerEntity);
        return customerPersistenceMapper.toDomain(saveEntity);
    }
}
