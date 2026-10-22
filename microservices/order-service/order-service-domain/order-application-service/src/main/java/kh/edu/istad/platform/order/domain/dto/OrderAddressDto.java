package kh.edu.istad.platform.order.domain.dto;

import java.util.UUID;

public record OrderAddressDto(
        UUID id,
        String street,
        String postalCode,
        String city
) {
}
