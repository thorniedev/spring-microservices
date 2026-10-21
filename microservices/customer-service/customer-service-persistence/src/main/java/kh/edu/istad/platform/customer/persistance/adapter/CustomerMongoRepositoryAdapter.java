package kh.edu.istad.platform.customer.persistance.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import kh.edu.istad.platform.customer.persistance.mapper.CustomerDocumentMapper;
import kh.edu.istad.platform.customer.persistance.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.persistance.repository.CustomerSegmentMongoRepository;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Repository
@ConditionalOnProperty(name = "persistence.db-type", havingValue = "mongo") // configure for change plug DB.
@RequiredArgsConstructor
public class CustomerMongoRepositoryAdapter implements CustomerRepository{

    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerDocumentMapper customerDocumentMapper;
    private final CustomerSegmentMongoRepository customerSegmentMongoRepository;

    @Override
    public Customer save(Customer customer) {

        // Domain -> Document
        CustomerDocument customerDocument = customerDocumentMapper.toDocument(customer);

        // Save to Mongo
        CustomerDocument savedDocument = customerMongoRepository.save(customerDocument).block();

        return customerDocumentMapper.toDomain(savedDocument);
    }

    @Override
    public Mono<Customer> save2 (Customer customer){
        return customerMongoRepository.save(
                customerDocumentMapper.toDocument(customer)
        ).map(customerDocumentMapper::toDomain);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerMongoRepository.findAll().map(
                customerDocumentMapper::toDomain
        );
    }

    @Override
    public Mono<Void> deleteById(CustomerId id) {
        return customerMongoRepository.deleteById(id.value().toString());
    }

    @Override
    public Mono<Customer> findById(CustomerId id) {
        return customerMongoRepository.findById(id.value().toString())
                .map(customerDocumentMapper::toDomain);
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        return customerMongoRepository.findByUsername(username)
                .map(customerDocumentMapper::toDomain);
    }

    // Dynamic Filter Implementation
    @Override
    public Mono<PaginatedResponse<Customer>> findByDynamicFilter(CustomerFilterCriteria criteria) {
        return customerMongoRepository.findByDynamicFilter(criteria)
                .map(page -> {
                    List<Customer> domainCustomers = page.content().stream()
                            .map(customerDocumentMapper::toDomain)
                            .toList();
                    return PaginatedResponse.of(
                            domainCustomers,
                            page.pageNumber(),
                            page.pageSize(),
                            page.totalElements()
                    );
                });
    }

    // CustomerSegment CRUD
    @Override
    public Mono<CustomerSegment> saveSegment(CustomerSegment segment) {
        return customerSegmentMongoRepository.save(customerDocumentMapper.toSegmentDocument(segment))
                .map(customerDocumentMapper::toSegmentDomain);
    }

    @Override
    public Flux<CustomerSegment> findAllSegments() {
        return customerSegmentMongoRepository.findAll()
                .map(customerDocumentMapper::toSegmentDomain);
    }

    @Override
    public Mono<CustomerSegment> findSegmentById(String segmentId) {
        return customerSegmentMongoRepository.findById(segmentId)
                .map(customerDocumentMapper::toSegmentDomain);
    }

    @Override
    public Flux<CustomerSegment> findSegmentsByIds(Set<String> segmentIds) {
        return customerSegmentMongoRepository.findAllById(segmentIds)
                .map(customerDocumentMapper::toSegmentDomain);
    }

    @Override
    public Mono<Void> deleteSegmentById(String segmentId) {
        return customerSegmentMongoRepository.deleteById(segmentId);
    }

    // Aggregation Endpoints Implementation
    @Override
    public Flux<CustomerStatusStatistic> aggregateCustomerStatusStatistics() {
        return customerMongoRepository.aggregateCustomerStatusStatistics();
    }

    @Override
    public Flux<CustomerSegmentReport> aggregateCustomerSegmentReport() {
        return customerMongoRepository.aggregateCustomerSegmentReport();
    }

    @Override
    public Flux<CustomerStatusAndSegmentStatistic> aggregateCustomerStatusAndSegmentStatistics(){
        return customerMongoRepository.aggregateCustomerStatusAndSegmentStatistics();
    }

    /**
     * 1. Dynamic Filter
     * 2. CustomerSegment(Silver, Gold, Platinum) @DbRef, @DocumentReference, (Manual)
     * 3. Aggregation customers/report, customers/stastic
     *  group by customerStatus
     *  group by customerSegment
     *  ... total, max, min,
     * */
}
