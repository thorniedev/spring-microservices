package kh.edu.istad.platform.customer.persistance.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {

    // Map Domain Customer => JPA CustomerEntity
    @Mapping(target = "customerId", source = "id")
    CustomerEntity toEntity(Customer customer);

    // JPA CustomerEntity => Domain Customer
    @Mapping(target = "id", source = "customerId")
    Customer toDomain(CustomerEntity customerEntity);

    // Type conversion for CustomerId <=> UUID
    default CustomerId toCustomerId(UUID value){
        return value != null ? new CustomerId(value) : null;
    }

    default UUID toUuid(CustomerId id) {
        return id != null ? id.value() : null;
    }
}
