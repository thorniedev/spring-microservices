package kh.edu.istad.platform.payment.valueobject;

import kh.edu.istad.common.domain.valueobject.BaseId;
import java.util.Objects;
import java.util.UUID;

public record CreditHistoryId(UUID value) implements BaseId<UUID> {
    public CreditHistoryId {
        Objects.requireNonNull(value, "CreditHistoryId value must not be null");
    }
}