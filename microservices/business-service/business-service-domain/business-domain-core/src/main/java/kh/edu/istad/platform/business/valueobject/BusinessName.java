package kh.edu.istad.platform.business.valueobject;

import java.util.Objects;
public record BusinessName(
        String legalName,
        String displayName
) {
    public BusinessName {
        Objects.requireNonNull(legalName, "Legal name must not be null");
        Objects.requireNonNull(displayName, "Display name must not be null");
    }
}
