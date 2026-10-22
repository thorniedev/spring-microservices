package kh.edu.istad.platform.order.domain.restapi.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record OrderAddressRequest(
        UUID id,
        @NotBlank(message = "Street is required")
        String street,
        @NotBlank(message = "Postal code is required")
        String postalCode,
        @NotBlank(message = "City is required")
        String city
) {
}
