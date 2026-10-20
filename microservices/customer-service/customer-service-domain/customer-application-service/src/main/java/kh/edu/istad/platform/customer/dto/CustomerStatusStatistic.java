package kh.edu.istad.platform.customer.dto;

import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerStatusStatistic(
        CustomerStatus customerStatus,
        Long totalCustomers,
        BigDecimal totalSpent,
        Double averageSpent,
        BigDecimal maxSpent,
        BigDecimal minSpent
) {
}
