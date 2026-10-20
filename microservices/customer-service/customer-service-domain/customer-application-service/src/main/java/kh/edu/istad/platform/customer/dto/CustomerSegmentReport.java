package kh.edu.istad.platform.customer.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerSegmentReport(
        String segmentType,
        Long totalCount,
        BigDecimal totalRevenue,
        Double averagePoints,
        BigDecimal maxPurchaseAmount,
        BigDecimal minPurchaseAmount

) {
}
