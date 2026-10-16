package kh.edu.istad.platform.customer.persistance.adapter;

import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import kh.edu.istad.platform.customer.persistance.repository.CustomerJpaRepository;
import kh.edu.istad.platform.customer.persistance.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistance.mapper.CustomerPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name= "persistence.db-type", havingValue = "postgres", matchIfMissing = true)
@RequiredArgsConstructor
public class CustomerJpaRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = customerPersistenceMapper.toEntity(customer);
        CustomerEntity saveEntity = customerJpaRepository.save(customerEntity).block();
        return customerPersistenceMapper.toDomain(saveEntity);
    }
}
