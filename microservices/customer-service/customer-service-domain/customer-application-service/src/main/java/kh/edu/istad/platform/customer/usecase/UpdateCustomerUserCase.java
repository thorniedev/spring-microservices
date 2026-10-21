package kh.edu.istad.platform.customer.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.dto.CustomerResult;
import kh.edu.istad.platform.customer.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import kh.edu.istad.platform.customer.execption.CustomerDomainException;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUserCase {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    public Mono<CustomerResult> updateCustomer(String id, UpdateCustomerCommand command) {
        log.info("Updating customer id: {} with data: {}", id, command);

        CustomerId customerId = parseCustomerId(id);

        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found with id: " + id)))
                .flatMap(c -> {
                    c.updateCustomer(command.familyName(), command.givenName(), command.phoneNumber(), command.segmentId());
                    customerDomainService.updateCustomer(c);
                    return customerRepository.save2(c);
                })
                .flatMap(this::enrichWithSegment);
    }

    public Mono<Void> deactivateCustomer(String id){
        log.info("Deactivating customer id: {}", id);

        CustomerId customerId = parseCustomerId(id);

        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found with id: " + id)))
                .flatMap(customer -> {
                    customerDomainService.deactivateCustomer(customer);
                    return customerRepository.save2(customer);
                })
                .then();
    }

    public Mono<Void> deleteCustomer(String id){
        log.info("Deleting customer id: {}", id);

        CustomerId customerId = parseCustomerId(id);

        return customerRepository.deleteById(customerId);
    }

    private CustomerId parseCustomerId(String id) {
        try {
            return new CustomerId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return new CustomerId(UUID.nameUUIDFromBytes(id.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }
    }

    private Mono<CustomerResult> enrichWithSegment(Customer customer) {
        if (customer.getSegmentId() == null || customer.getSegmentId().isBlank()) {
            return Mono.just(toResult(customer, null));
        }
        return customerRepository.findSegmentById(customer.getSegmentId())
                .map(segment -> toResult(customer, segment))
                .defaultIfEmpty(toResult(customer, null));
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
}
