package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateBusinessRequest(
        @NotBlank(message = "Legal name is required")
        String legalName,

        @NotBlank(message = "Display name is required")
        String displayName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @Valid
        List<ProductRequest> products
) {
}