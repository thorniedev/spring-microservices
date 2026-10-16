package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.payment.entity.Payment;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.PaymentJpaRepository;
import kh.edu.istad.platform.payment.port.out.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentJpaRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    public Payment save(Payment payment) {
        return paymentPersistenceMapper.toPaymentDomain(
                paymentJpaRepository.save(paymentPersistenceMapper.toPaymentEntity(payment))
        );
    }

    @Override
    public Optional<Payment> findByOrderId(OrderId orderId) {
        return paymentJpaRepository.findByOrderId(orderId.getValue())
                .map(paymentPersistenceMapper::toPaymentDomain);
    }
}