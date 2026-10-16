package kh.edu.istad.platform.business.valueobject;

import java.util.Objects;

public record BusinessContact(
        String email,
        String phoneNumber
) {
    public BusinessContact {
        Objects.requireNonNull(email, "Email must not be null");
        Objects.requireNonNull(phoneNumber, "Phone number must not be null");
    }
}