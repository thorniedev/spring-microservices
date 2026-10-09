package kh.edu.istad.platform.customer.usecase;

import kh.edu.istad.platform.customer.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitiateCustomerUseCase
{
    // Spring will automatically inject whichever adapter is active (JPA or Mongo)!

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository; // dependency point inward (D principle)

    public InitiateCustomerResult execute(InitiateCustomerCommand command)
    {
        log.info("Initiate Customer UseCase: {}", command);

        // validate by load data from persistence layer (output port)
        // invoke domain logic (call domain service)
        // customerDomainService.initiateCustomer()
        // sava data to database (output port)
        // customerRepository.save();

        // Build Domain Entity from command
        Customer customer = Customer.Builder.builder()
                .username(command.username())
                .familyName(command.familyName())
                .givenName(command.givenName())
                .email(command.email())
                .phoneNumber(command.phoneNumber())
                .build();

        // Invoke domain logic (Validates, generates UUID, sets status to ACTIVE)
        customerDomainService.initiateCustomer(customer);

        // save to database through outbound port (Persistence adapter)
        Customer savedCustomer = customerRepository.save(customer);

        // return result with generated customer ID
        return new InitiateCustomerResult(
                savedCustomer.getId().value(),
                savedCustomer.getUsername(),
                savedCustomer.getFamilyName(),
                savedCustomer.getGivenName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhoneNumber()
                );
    }
}
