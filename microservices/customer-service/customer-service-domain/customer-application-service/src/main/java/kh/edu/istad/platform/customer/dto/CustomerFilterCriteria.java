package kh.edu.istad.platform.customer.dto;

import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.valueobject.SegmentType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerFilterCriteria(
        String firstName,
        String lastName,
        String email,
        CustomerStatus customerStatus,
        SegmentType segmentName,
        BigDecimal minTotalSpent,
        BigDecimal maxTotalSpent,
        Integer page,
        Integer size,
        String sortBy,
        String direction
) {
    public CustomerFilterCriteria {
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size <= 0 || size > 100) ? 10 : size;
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        direction = (direction == null || (!direction.equalsIgnoreCase("ASC") && !direction.equalsIgnoreCase("DESC")))
                ? "DESC" : direction.toUpperCase();
    }
}
