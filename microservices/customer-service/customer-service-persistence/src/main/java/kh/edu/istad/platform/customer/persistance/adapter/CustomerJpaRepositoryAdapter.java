package kh.edu.istad.platform.customer.persistance.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.dto.CustomerFilterCriteria;
import kh.edu.istad.platform.customer.dto.CustomerSegmentReport;
import kh.edu.istad.platform.customer.dto.CustomerStatusStatistic;
import kh.edu.istad.platform.customer.dto.PaginatedResponse;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import kh.edu.istad.platform.customer.persistance.repository.CustomerJpaRepository;
import kh.edu.istad.platform.customer.persistance.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistance.mapper.CustomerPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

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

    @Override
    public Mono<Customer> save2(Customer customer) {
        return null;
    }

    @Override
    public Flux<Customer> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(CustomerId id) {
        return null;
    }

    @Override
    public Mono<Customer> findById(CustomerId id) {
        return null;
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        return null;
    }

    @Override
    public Mono<PaginatedResponse<Customer>> findByDynamicFilter(CustomerFilterCriteria criteria) {
        return null;
    }

    @Override
    public Mono<CustomerSegment> findSegmentById(String segmentId) {
        return null;
    }

    @Override
    public Flux<CustomerSegment> findSegmentsByIds(Set<String> segmentIds) {
        return null;
    }

    @Override
    public Flux<CustomerStatusStatistic> aggregateCustomerStatusStatistics() {
        return null;
    }

    @Override
    public Flux<CustomerSegmentReport> aggregateCustomerSegmentReport() {
        return null;
    }

}
