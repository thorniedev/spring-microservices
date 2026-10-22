package kh.edu.istad.platform.order.domain.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.domain.restapi.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.domain.restapi.mapper.OrderWebMapper;
import kh.edu.istad.platform.order.domain.usecase.CreateOrderUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderWebMapper orderWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        log.info("Received request to create order for customer: {}", createOrderRequest.customerId());
        CreateOrderResult result = createOrderUseCase.execute(orderWebMapper.toCommand(createOrderRequest));
        return orderWebMapper.toResponse(result);
    }
}
