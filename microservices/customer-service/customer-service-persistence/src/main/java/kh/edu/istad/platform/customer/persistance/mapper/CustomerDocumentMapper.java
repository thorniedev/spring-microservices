package kh.edu.istad.platform.customer.persistance.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.entity.CustomerSegment;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistance.document.CustomerSegmentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) //unmappedTargetPolicy = ReportingPolicy.IGNORE
public interface CustomerDocumentMapper
{
    // Domain -> Mongo Document
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "totalSpent", source = "totalSpent.amount")
    @Mapping(target = "segmentDbRef", ignore = true)
    @Mapping(target = "segmentDocRef", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    CustomerDocument toDocument(Customer customer);

    // Mongo Document -> Domain
    @Mapping(target = "id", source = "id")
    @Mapping(target = "totalSpent", source = "totalSpent")
    Customer toDomain(CustomerDocument  customerDocument);

    @Mapping(target = "minSpend", source = "minSpend.amount")
    CustomerSegmentDocument toSegmentDocument(CustomerSegment segment);

    @Mapping(target = "minSpend", source = "minSpend")
    CustomerSegment toSegmentDomain(CustomerSegmentDocument document);

    default CustomerId mapStringToCustomerId(String value) {
        if (value == null) {
            return null;
        }
        try {
            return new CustomerId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            return new CustomerId(UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8)));
        }
    }
    default String mapCustomerIdToString(CustomerId customerId) {
        if (customerId == null || customerId.value() == null) {
            return null;
        }
        return customerId.value().toString();
    }
    default Money mapMoney(BigDecimal amount) {
        return amount != null ? new Money(amount) : Money.ZERO;
    }
    default BigDecimal mapMoneyToBigDecimal(Money money) {
        return money != null ? money.amount() : BigDecimal.ZERO;
    }
}
