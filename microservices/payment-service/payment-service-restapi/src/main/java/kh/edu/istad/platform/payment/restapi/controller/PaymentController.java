package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.payment.dto.ProcessPaymentResult;
import kh.edu.istad.platform.payment.restapi.dto.PaymentRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentResponse;
import kh.edu.istad.platform.payment.restapi.mapper.PaymentWebMapper;
import kh.edu.istad.platform.payment.usecase.ProcessPaymentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final PaymentWebMapper paymentWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse processPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Received request to process payment for order: {}", request.orderId());
        ProcessPaymentResult result = processPaymentUseCase.execute(paymentWebMapper.toCommand(request));
        return paymentWebMapper.toResponse(result);
    }
}