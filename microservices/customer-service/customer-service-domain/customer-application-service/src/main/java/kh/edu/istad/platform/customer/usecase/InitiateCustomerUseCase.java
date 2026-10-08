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
        return new InitiateCustomerResult(UUID.randomUUID());
    }
}
