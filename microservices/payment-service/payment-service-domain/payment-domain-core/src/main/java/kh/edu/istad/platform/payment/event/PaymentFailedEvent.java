package kh.edu.istad.platform.payment.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.payment.entity.Payment;
import java.time.ZonedDateTime;
import java.util.List;

public record PaymentFailedEvent(
        Payment payment,
        ZonedDateTime createdAt,
        List<String> failureMessages
) implements DomainEvent<Payment> {
}