package kh.edu.istad.platform.payment.port.out;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.payment.entity.Payment;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(OrderId orderId);
}