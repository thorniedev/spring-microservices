package kh.edu.istad.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) implements BaseId<UUID> {
    public OrderId {
        Objects.requireNonNull(value, "OrderId cannot be null");
    }
}
