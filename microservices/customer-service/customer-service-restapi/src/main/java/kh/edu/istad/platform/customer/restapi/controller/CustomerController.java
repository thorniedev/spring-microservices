package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.restapi.dto.CustomerResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.usecase.InitiateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import kh.edu.istad.platform.customer.usecase.QueryCustomerUseCase;
import kh.edu.istad.platform.customer.usecase.UpdateCustomerUserCase;
import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.valueobject.SegmentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;
    private final QueryCustomerUseCase queryCustomerUseCase;
    private final UpdateCustomerUserCase updateCustomerUserCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerInitiateResponse> initiateCustomer(
            @RequestBody CustomerInitiateRequest customerInitiateRequest
    ) {
         // convert request to command
         InitiateCustomerCommand command = customerWebMapper.toCommand(customerInitiateRequest);

         // call usecase
        return initiateCustomerUseCase.execute(command)
                .map(customerWebMapper::toResponse);

    }

    // READ All with Dynamic Filtering, Reactive Pagination & Sorting
    // Dynamic Filter with Reactive Pagination & Sorting
    public Mono<PaginatedResponse<CustomerResponse>> getCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) CustomerStatus customerStatus,
            @RequestParam(required = false) SegmentType segmentName,
            @RequestParam(required = false) BigDecimal minTotalSpent,
            @RequestParam(required = false) BigDecimal maxTotalSpent,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        CustomerFilterCriteria criteria = CustomerFilterCriteria.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .customerStatus(customerStatus)
                .segmentName(segmentName)
                .minTotalSpent(minTotalSpent)
                .maxTotalSpent(maxTotalSpent)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        return queryCustomerUseCase.executeFilter(criteria)
                //.map(this::mapToWebResponse);
                .map(customerWebMapper::toPaginatedResponse); // Replace it with mapToWebResponse
    }

    // READ Single Customer By ID
    @GetMapping("/{id}")
    public Mono<CustomerResponse> getCustomerById(@PathVariable String id) {
        return queryCustomerUseCase.findById(id)
                .map(customerWebMapper::toResponse);
    }

    // UPDATE Customer
    @PutMapping("/{id}")
    public Mono<CustomerResponse> updateCustomer(
            @PathVariable String id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        UpdateCustomerCommand command = customerWebMapper.toCommand(request);
        return updateCustomerUserCase.updateCustomer(id, command)
                .map(customerWebMapper::toResponse);
    }

    // DELETE / Deactivate Customer (Soft Delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deactivateCustomer(@PathVariable String id) {
        return updateCustomerUserCase.deactivateCustomer(id);
    }

    // Reactive Status Statistics Aggregation
    @GetMapping(value = "/stastic", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CustomerStatusStatistic> getCustomerStatusStatistics() {
        return queryCustomerUseCase.getCustomerStatusStatistics();
    }

    // Alias for standard spelling
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CustomerStatusStatistic> getCustomerStatusStatisticsAlias() {
        return queryCustomerUseCase.getCustomerStatusStatistics();
    }

    // Reactive Customer Segment updateCustomerUserCasePerformance Report Aggregation

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CustomerSegmentReport> getCustomerSegmentReport() {
        return queryCustomerUseCase.getCustomerSegmentReport();
    }

    //
//    private PaginatedResponse<CustomerResponse> mapToWebResponse(PaginatedResponse<CustomerResult> page) {
//        List<CustomerResponse> webContent = page.content().stream()
//                .map(r -> new CustomerResponse(
//                        r.id(),
//                        r.username(),
//                        r.familyName(),
//                        r.givenName(),
//                        r.email(),
//                        r.phoneNumber(),
//                        r.status(),
//                        r.totalSpent(),
//                        r.loyaltyPoints(),
//                        r.segmentId(),
//                        r.segmentType(),
//                        r.discountRate()
//                ))
//                .toList();
//        return PaginatedResponse.of(
//                webContent,
//                page.pageNumber(),
//                page.pageSize(),
//                page.totalElements()
//        );
//    }
}
