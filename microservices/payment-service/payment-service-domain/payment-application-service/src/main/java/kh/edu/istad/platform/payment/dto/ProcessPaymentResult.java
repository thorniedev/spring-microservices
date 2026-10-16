package kh.edu.istad.platform.payment.dto;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ProcessPaymentResult(
        UUID paymentId,
        UUID orderId,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt,
        List<String> failureMessages
) {
}