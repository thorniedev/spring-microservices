package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.dto.CustomerSegmentCommand;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentResponse;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import kh.edu.istad.platform.customer.usecase.CustomerSegmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/segments")
public class CustomerSegmentController {

    private final CustomerSegmentUseCase customerSegmentUseCase;
    private final CustomerWebMapper customerWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerSegmentResponse> createSegment(@Valid @RequestBody CustomerSegmentRequest request) {
        CustomerSegmentCommand command = customerWebMapper.toCommand(request);
        return customerSegmentUseCase.create(command)
                .map(customerWebMapper::toResponse);
    }

    @GetMapping
    public Flux<CustomerSegmentResponse> getAllSegments() {
        return customerSegmentUseCase.findAll()
                .map(customerWebMapper::toResponse);
    }
    @GetMapping("/{id}")
    public Mono<CustomerSegmentResponse> getSegmentById(@PathVariable String id) {
        return customerSegmentUseCase.findById(id)
                .map(customerWebMapper::toResponse);
    }
    @PutMapping("/{id}")
    public Mono<CustomerSegmentResponse> updateSegment(
            @PathVariable String id,
            @Valid @RequestBody CustomerSegmentRequest request
    ) {
        CustomerSegmentCommand command = customerWebMapper.toCommand(request);
        return customerSegmentUseCase.update(id, command)
                .map(customerWebMapper::toResponse);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteSegment(@PathVariable String id) {
        return customerSegmentUseCase.deleteById(id);
    }

}
