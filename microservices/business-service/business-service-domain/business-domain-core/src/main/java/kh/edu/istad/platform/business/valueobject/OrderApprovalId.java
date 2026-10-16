package kh.edu.istad.platform.business.valueobject;

import kh.edu.istad.common.domain.valueobject.BaseId;
import java.util.Objects;
import java.util.UUID;

public record OrderApprovalId(UUID value) implements BaseId<UUID> {
    public OrderApprovalId {
        Objects.requireNonNull(value, "OrderApprovalId value must not be null");
    }
}