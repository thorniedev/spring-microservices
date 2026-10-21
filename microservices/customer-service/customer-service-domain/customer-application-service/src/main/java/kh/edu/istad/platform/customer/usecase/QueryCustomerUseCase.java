package kh.edu.istad.platform.customer.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryCustomerUseCase {

    private final CustomerRepository customerRepository;

    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Mono<CustomerResult> findById(String id) {
        CustomerId customerId = parseCustomerId(id);
        return customerRepository.findById(customerId)
                .flatMap(customer -> {
                    if (customer.getSegmentId() == null || customer.getSegmentId().isBlank()) {
                        return Mono.just(toResult(customer, null));
                    }
                    return customerRepository.findSegmentById(customer.getSegmentId())
                            .map(segment -> toResult(customer, segment))
                            .defaultIfEmpty(toResult(customer, null));
                });
    }

    // Dynamic filter + non-blocking enrichment of CustomerSegment (Manual Reference)
    public Mono<PaginatedResponse<CustomerResult>> executeFilter(CustomerFilterCriteria criteria) {
        return customerRepository.findByDynamicFilter(criteria)
                .flatMap(this::enrichWithSegmentDetails);
    }

    private Mono<PaginatedResponse<CustomerResult>> enrichWithSegmentDetails(PaginatedResponse<Customer> paginatedCustomers) {
        Set<String> segmentIds = paginatedCustomers.content().stream()
                .map(Customer::getSegmentId)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());
        if (segmentIds.isEmpty()) {
            List<CustomerResult> results = paginatedCustomers.content().stream()
                    .map(c -> toResult(c, null))
                    .toList();
            return Mono.just(PaginatedResponse.of(
                    results,
                    paginatedCustomers.pageNumber(),
                    paginatedCustomers.pageSize(),
                    paginatedCustomers.totalElements()
            ));
        }

        // Pure Reactive batch fetch using Project Reactor
        return customerRepository.findSegmentsByIds(segmentIds)
                .collectMap(CustomerSegment::getId, segment -> segment)
                .map(segmentMap -> {
                    List<CustomerResult> results = paginatedCustomers.content().stream()
                            .map(c -> toResult(c, segmentMap.get(c.getSegmentId())))
                            .toList();
                    return PaginatedResponse.of(
                            results,
                            paginatedCustomers.pageNumber(),
                            paginatedCustomers.pageSize(),
                            paginatedCustomers.totalElements()
                    );
                });
    }

    private CustomerId parseCustomerId(String id) {
        try {
            return new CustomerId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return new CustomerId(UUID.nameUUIDFromBytes(id.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }
    }

    private CustomerResult toResult(Customer c, CustomerSegment segment) {
        return new CustomerResult(
                c.getId() != null ? c.getId().getValue().toString() : null,
                c.getUsername(),
                c.getFamilyName(),
                c.getGivenName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.getStatus(),
                c.getTotalSpent() != null ? c.getTotalSpent().amount() : BigDecimal.ZERO,
                c.getLoyaltyPoints(),
                c.getSegmentId(),
                segment != null ? segment.getSegmentType() : null,
                segment != null ? segment.getDiscountRate() : BigDecimal.ZERO
        );
    }


    public Flux<CustomerStatusStatistic> getCustomerStatusStatistics() {
        return customerRepository.aggregateCustomerStatusStatistics();
    }

    public Flux<CustomerSegmentReport> getCustomerSegmentReport() {
        return customerRepository.aggregateCustomerSegmentReport();
    }

    public Flux<CustomerStatusAndSegmentStatistic> getCustomerStatusAndSegmentStatistics() {
        return customerRepository.aggregateCustomerStatusAndSegmentStatistics();
    }

}
