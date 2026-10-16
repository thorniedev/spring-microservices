package kh.edu.istad.platform.payment.valueobject;

import kh.edu.istad.common.domain.valueobject.BaseId;
import java.util.Objects;
import java.util.UUID;

public record PaymentId(UUID value) implements BaseId<UUID> {
    public PaymentId {
        Objects.requireNonNull(value, "PaymentId value must not be null");
    }
}