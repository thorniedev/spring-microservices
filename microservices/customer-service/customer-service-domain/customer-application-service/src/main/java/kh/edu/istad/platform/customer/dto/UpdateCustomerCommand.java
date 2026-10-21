package kh.edu.istad.platform.customer.dto;

import lombok.Builder;

@Builder
public record UpdateCustomerCommand(
        String familyName,
        String givenName,
        String phoneNumber,
        String segmentId
) {
}
