package kh.edu.istad.platform.customer.restapi.dto;

import kh.edu.istad.platform.customer.valueobject.SegmentType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CustomerSegmentResponse(
        String id,
        SegmentType segmentType,
        List<String> benefits,
        Integer minPoints,
        BigDecimal minSpend,
        BigDecimal discountRate
) {
}
