package kh.edu.istad.platform.customer.persistance.enity;

import jakarta.persistence.*;
import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @Column(name = "customer_id", updatable = false, nullable = false)
    private UUID customerId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status;
}
