package kh.edu.istad.platform.payment.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.payment.entity.CreditEntry;
import kh.edu.istad.platform.payment.entity.CreditHistory;
import kh.edu.istad.platform.payment.entity.Payment;
import kh.edu.istad.platform.payment.persistence.entity.CreditEntryEntity;
import kh.edu.istad.platform.payment.persistence.entity.CreditHistoryEntity;
import kh.edu.istad.platform.payment.persistence.entity.PaymentEntity;
import kh.edu.istad.platform.payment.valueobject.CreditEntryId;
import kh.edu.istad.platform.payment.valueobject.CreditHistoryId;
import kh.edu.istad.platform.payment.valueobject.PaymentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PaymentPersistenceMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "orderId", source = "orderId.value")
    @Mapping(target = "customerId", source = "customerId.value")
    @Mapping(target = "price", source = "price.amount")
    PaymentEntity toPaymentEntity(Payment payment);

    @Mapping(target = "paymentId", source = "id")
    @Mapping(target = "price", source = "price")
    Payment toPaymentDomain(PaymentEntity entity);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "customerId", source = "customerId.value")
    @Mapping(target = "totalCreditAmount", source = "totalCreditAmount.amount")
    CreditEntryEntity toCreditEntryEntity(CreditEntry creditEntry);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "totalCreditAmount", source = "totalCreditAmount")
    CreditEntry toCreditEntryDomain(CreditEntryEntity entity);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "customerId", source = "customerId.value")
    @Mapping(target = "amount", source = "amount.amount")
    CreditHistoryEntity toCreditHistoryEntity(CreditHistory creditHistory);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    CreditHistory toCreditHistoryDomain(CreditHistoryEntity entity);

    default PaymentId mapPaymentId(UUID val) { return val != null ? new PaymentId(val) : null; }
    default OrderId mapOrderId(UUID val) { return val != null ? new OrderId(val) : null; }
    default CustomerId mapCustomerId(UUID val) { return val != null ? new CustomerId(val) : null; }
    default CreditEntryId mapCreditEntryId(UUID val) { return val != null ? new CreditEntryId(val) : null; }
    default CreditHistoryId mapCreditHistoryId(UUID val) { return val != null ? new CreditHistoryId(val) : null; }
    default Money mapMoney(BigDecimal val) { return val != null ? new Money(val) : null; }
}