package kh.edu.istad.platform.order.valueobject;

import java.util.Objects;
import java.util.UUID;

public record StreetAddress(
        UUID id,
        String street,
        String postalCode,
        String city
) {
    public StreetAddress {
        Objects.requireNonNull(id, "StreetAddress id must not be null");
        Objects.requireNonNull(street, "Street must not be null");
        Objects.requireNonNull(postalCode, "Postal code must not be null");
        Objects.requireNonNull(city, "City must not be null");
    }
}
