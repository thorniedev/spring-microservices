package kh.edu.istad.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record BusinessId(UUID value) implements BaseId<UUID> {
    public BusinessId {
        Objects.requireNonNull(value, "CustomerId value must not be null");
    }
}
