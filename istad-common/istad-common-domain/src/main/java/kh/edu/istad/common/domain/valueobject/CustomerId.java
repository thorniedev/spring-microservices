package kh.edu.istad.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) implements BaseId<UUID>{
    public CustomerId{
        Objects.requireNonNull(value, "CustomerId value must not be null");
    }
}
