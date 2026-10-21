package kh.edu.istad.platform.customer.dto;

import kh.edu.istad.platform.customer.valueobject.SegmentType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CustomerSegmentCommand(
        String id,
        SegmentType segmentType,
        List<String> benefits,
        Integer minPoints,
        BigDecimal minSpend,
        BigDecimal discountRate
) {
}
