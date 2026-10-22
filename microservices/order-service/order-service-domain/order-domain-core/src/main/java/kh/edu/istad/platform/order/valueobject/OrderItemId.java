package kh.edu.istad.platform.order.valueobject;

import kh.edu.istad.common.domain.valueobject.BaseId;

import java.util.Objects;
import java.util.UUID;

public record OrderItemId(
        UUID value
)
        implements BaseId<UUID> {
             public OrderItemId {
             Objects.requireNonNull(value, "OrderItemId cannot be null");
        }
}
