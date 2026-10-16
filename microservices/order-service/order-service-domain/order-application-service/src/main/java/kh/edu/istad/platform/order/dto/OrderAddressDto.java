package kh.edu.istad.platform.order.dto;

import java.util.UUID;

public record OrderAddressDto(
        UUID id,
        String street,
        String postalCode,
        String city
) {
}
