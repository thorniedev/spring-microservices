package kh.edu.istad.platform.customer.restapi.dto;

import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.valueobject.SegmentType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerResponse(
        String id,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        CustomerStatus status,
        BigDecimal totalSpent,
        Integer loyaltyPoints,
        String segmentId,
        SegmentType segmentType,
        BigDecimal discountRate
) {
}
