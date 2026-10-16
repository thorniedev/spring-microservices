package kh.edu.istad.platform.business.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessRequest;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessResponse;
import kh.edu.istad.platform.business.restapi.mapper.BusinessWebMapper;
import kh.edu.istad.platform.business.usecase.CreateBusinessUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/businesses")
public class BusinessController {

    private final CreateBusinessUseCase createBusinessUseCase;
    private final BusinessWebMapper businessWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreateBusinessResponse> createBusiness(@Valid @RequestBody CreateBusinessRequest request) {
        log.info("Received reactive request to register business: {}", request.displayName());
        return createBusinessUseCase.execute(businessWebMapper.toCommand(request))
                .map(businessWebMapper::toResponse);
    }
}