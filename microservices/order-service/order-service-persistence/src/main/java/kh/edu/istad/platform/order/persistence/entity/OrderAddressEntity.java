package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_addresses")
public class OrderAddressEntity {
    @Id
    private UUID id;
    private String street;
    private String postalCode;
    private String city;
}
