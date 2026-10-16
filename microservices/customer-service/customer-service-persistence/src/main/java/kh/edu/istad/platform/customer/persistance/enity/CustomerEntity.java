package kh.edu.istad.platform.customer.persistance.enity;

import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table("customers")
public class CustomerEntity {

    @Id
    private UUID customerId;

    private String username;

    private String familyName;

    private String givenName;

    private String email;

    private String phoneNumber;

    private CustomerStatus status;
}
