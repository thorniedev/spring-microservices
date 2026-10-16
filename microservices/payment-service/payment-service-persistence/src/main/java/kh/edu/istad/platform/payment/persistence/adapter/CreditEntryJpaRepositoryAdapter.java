package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.entity.CreditEntry;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditEntryJpaRepository;
import kh.edu.istad.platform.payment.port.out.CreditEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CreditEntryJpaRepositoryAdapter implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return paymentPersistenceMapper.toCreditEntryDomain(
                creditEntryJpaRepository.save(paymentPersistenceMapper.toCreditEntryEntity(creditEntry))
        );
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository.findByCustomerId(customerId.getValue())
                .map(paymentPersistenceMapper::toCreditEntryDomain);
    }
}