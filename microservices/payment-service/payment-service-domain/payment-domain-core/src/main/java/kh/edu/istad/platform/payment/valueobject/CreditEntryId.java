package kh.edu.istad.platform.payment.valueobject;

import kh.edu.istad.common.domain.valueobject.BaseId;
import java.util.Objects;
import java.util.UUID;

public record CreditEntryId(UUID value) implements BaseId<UUID> {
    public CreditEntryId {
        Objects.requireNonNull(value, "CreditEntryId value must not be null");
    }
}