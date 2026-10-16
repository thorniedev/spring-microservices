package kh.edu.istad.platform.payment.persistence.entity;

import jakarta.persistence.*;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private UUID id;
    private UUID orderId;
    private UUID customerId;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private ZonedDateTime createdAt;
}