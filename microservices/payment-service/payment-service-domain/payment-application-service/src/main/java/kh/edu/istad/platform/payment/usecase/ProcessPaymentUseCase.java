package kh.edu.istad.platform.payment.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.payment.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.payment.dto.ProcessPaymentResult;
import kh.edu.istad.platform.payment.entity.CreditEntry;
import kh.edu.istad.platform.payment.entity.CreditHistory;
import kh.edu.istad.platform.payment.entity.Payment;
import kh.edu.istad.platform.payment.event.PaymentCompletedEvent;
import kh.edu.istad.platform.payment.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.service.PaymentDomainService;
import kh.edu.istad.platform.payment.valueobject.CreditEntryId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    @Transactional
    public ProcessPaymentResult execute(ProcessPaymentCommand command) {
        log.info("Processing payment for order: {} customer: {}", command.orderId(), command.customerId());

        Payment payment = Payment.builder()
                .orderId(new OrderId(command.orderId()))
                .customerId(new CustomerId(command.customerId()))
                .price(new Money(command.price()))
                .build();

        // Find customer credit or create an initial balance if new
        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(payment.getCustomerId())
                .orElseGet(() -> new CreditEntry(new CreditEntryId(UUID.randomUUID()), payment.getCustomerId(), new Money(BigDecimal.valueOf(1000.00))));

        List<CreditHistory> creditHistories = new ArrayList<>(creditHistoryRepository.findByCustomerId(payment.getCustomerId()));
        List<String> failureMessages = new ArrayList<>();

        PaymentCompletedEvent event = paymentDomainService.validateAndInitiatePayment(
                payment, creditEntry, creditHistories, failureMessages
        );

        Payment savedPayment = paymentRepository.save(event.payment());
        creditEntryRepository.save(creditEntry);
        creditHistories.forEach(creditHistoryRepository::save);

        log.info("Payment processed with status: {}", savedPayment.getPaymentStatus());

        return new ProcessPaymentResult(
                savedPayment.getId().getValue(),
                savedPayment.getOrderId().getValue(),
                savedPayment.getPaymentStatus(),
                savedPayment.getCreatedAt(),
                failureMessages
        );
    }
}