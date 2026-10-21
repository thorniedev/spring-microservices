package kh.edu.istad.platform.customer.usecase;


import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.customer.dto.CustomerSegmentCommand;
import kh.edu.istad.platform.customer.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import kh.edu.istad.platform.customer.execption.CustomerDomainException;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerSegmentUseCase {

    private final CustomerRepository customerRepository;


    public Mono<CustomerSegmentResult> create(CustomerSegmentCommand command) {
        String segmentId = (command.id() != null && !command.id().isBlank())
                ? command.id()
                : UUID.randomUUID().toString();
        CustomerSegment segment = new CustomerSegment(
                segmentId,
                command.segmentType(),
                command.benefits(),
                command.minPoints(),
                command.minSpend() != null ? new Money(command.minSpend()) : Money.ZERO,
                command.discountRate()
        );
        return customerRepository.saveSegment(segment)
                .map(this::toResult);
    }

    public Flux<CustomerSegmentResult> findAll() {
        return customerRepository.findAllSegments()
                .map(this::toResult);
    }

    public Mono<CustomerSegmentResult> findById(String id) {
        return customerRepository.findSegmentById(id)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found with id: " + id)))
                .map(this::toResult);
    }


    public Mono<CustomerSegmentResult> update(String id, CustomerSegmentCommand command) {
        return customerRepository.findSegmentById(id)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found with id: " + id)))
                .flatMap(existing -> {
                    existing.updateSegment(
                            command.segmentType(),
                            command.benefits(),
                            command.minPoints(),
                            command.minSpend() != null ? new Money(command.minSpend()) : Money.ZERO,
                            command.discountRate()
                    );
                    return customerRepository.saveSegment(existing);
                })
                .map(this::toResult);
    }

    public Mono<Void> deleteById(String id) {
        return customerRepository.deleteSegmentById(id);
    }
    private CustomerSegmentResult toResult(CustomerSegment s) {
        return new CustomerSegmentResult(
                s.getId(),
                s.getSegmentType(),
                s.getBenefits(),
                s.getMinPoints(),
                s.getMinSpend() != null ? s.getMinSpend().amount() : null,
                s.getDiscountRate()
        );
    }

}
