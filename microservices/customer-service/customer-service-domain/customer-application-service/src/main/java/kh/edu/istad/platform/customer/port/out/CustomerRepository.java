package kh.edu.istad.platform.customer.port.out;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

// Domain Repository Pattern (DDD)
public interface CustomerRepository
{
    Customer save(Customer customer);

    Mono<Customer> save2(Customer customer);

    Flux<Customer> findAll();

    Mono<Void> deleteById(CustomerId id);

    Mono<Customer> findById(CustomerId id);

    Mono<Customer> findByUsername(String username);

    // Dynamic Filter with Reactive Pagination
    Mono<PaginatedResponse<Customer>> findByDynamicFilter(CustomerFilterCriteria criteria);

    // Manual Reference Lookup for Segments
    Mono<CustomerSegment> findSegmentById(String segmentId);
    Flux<CustomerSegment> findSegmentsByIds(Set<String> segmentIds);

    default Mono<CustomerSegment> saveSegment(CustomerSegment segment) {
        return Mono.empty();
    }
    default Flux<CustomerSegment> findAllSegments() {
        return Flux.empty();
    }
    default Mono<Void> deleteSegmentById(String segmentId) {
        return Mono.empty();
    }

    // Aggregations
    Flux<CustomerStatusStatistic> aggregateCustomerStatusStatistics();

    Flux<CustomerSegmentReport> aggregateCustomerSegmentReport();

    default Flux<CustomerStatusAndSegmentStatistic> aggregateCustomerStatusAndSegmentStatistics() {
        return Flux.empty();
    }

}
