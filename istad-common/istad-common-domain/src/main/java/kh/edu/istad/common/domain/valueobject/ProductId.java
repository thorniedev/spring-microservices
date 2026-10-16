package kh.edu.istad.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) implements BaseId<UUID> {
    public ProductId {
        Objects.requireNonNull(value, "ProductId cannot be null");
    }
}
