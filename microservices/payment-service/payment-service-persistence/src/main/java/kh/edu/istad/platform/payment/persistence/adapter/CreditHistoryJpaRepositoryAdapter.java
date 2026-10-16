package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.entity.CreditHistory;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditHistoryJpaRepository;
import kh.edu.istad.platform.payment.port.out.CreditHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CreditHistoryJpaRepositoryAdapter implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return paymentPersistenceMapper.toCreditHistoryDomain(
                creditHistoryJpaRepository.save(paymentPersistenceMapper.toCreditHistoryEntity(creditHistory))
        );
    }

    @Override
    public List<CreditHistory> findByCustomerId(CustomerId customerId) {
        return creditHistoryJpaRepository.findByCustomerId(customerId.getValue()).stream()
                .map(paymentPersistenceMapper::toCreditHistoryDomain)
                .toList();
    }
}