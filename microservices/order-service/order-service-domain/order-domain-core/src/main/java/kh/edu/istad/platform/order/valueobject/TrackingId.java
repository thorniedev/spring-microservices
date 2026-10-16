package kh.edu.istad.platform.order.valueobject;

import kh.edu.istad.common.domain.valueobject.BaseId;

import java.util.Objects;
import java.util.UUID;

public record TrackingId(
        UUID value
) implements BaseId<UUID> {
    public TrackingId {
        Objects.requireNonNull(value, "TrackingId value must not be null");
    }
}
