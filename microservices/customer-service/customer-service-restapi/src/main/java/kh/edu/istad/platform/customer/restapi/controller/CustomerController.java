package kh.edu.istad.platform.customer.restapi.controller;

import kh.edu.istad.platform.customer.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.usecase.InitiateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerInitiateResponse initiateCustomer(
            @RequestBody CustomerInitiateRequest customerInitiateRequest
    ) {
         InitiateCustomerResult result = initiateCustomerUseCase.execute(
                customerWebMapper.toCommand(customerInitiateRequest)
        );
        return customerWebMapper.toResponse(result);
    }
}
