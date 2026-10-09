package kh.edu.istad.platform.customer.persistance.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerDocumentMapper
{
    // Domain -> Mongo Document
    CustomerDocument toDocument(Customer customer);

    // Mongo Document -> Domain
    Customer toDomain(CustomerDocument document);

    // Type converters (if CustomerDocument uses UUID id instead of CustomerId)
    default CustomerId toCustomerId(UUID value) {
        return value != null ? new CustomerId(value) : null;
    }

    default UUID toUuid(CustomerId id) {
        return id != null ? id.value() : null;
    }
}
