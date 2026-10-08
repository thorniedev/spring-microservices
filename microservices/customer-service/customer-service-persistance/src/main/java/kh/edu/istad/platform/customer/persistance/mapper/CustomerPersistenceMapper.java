package kh.edu.istad.platform.customer.persistance.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {

    // Map Domain Customer => JPA CustomerEntity
    @Mapping(target = "customerId", source = "id.value")
    CustomerEntity toEntity(Customer customer);

    // Default method to build the Domain Entity using its Builder
    default Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }
        return Customer.Builder.builder()
                .id(entity.getCustomerId() != null ? new CustomerId(entity.getCustomerId()) : null)
                .username(entity.getUsername())
                .familyName(entity.getFamilyName())
                .givenName(entity.getGivenName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .status(entity.getStatus())
                .build();
    }
}
