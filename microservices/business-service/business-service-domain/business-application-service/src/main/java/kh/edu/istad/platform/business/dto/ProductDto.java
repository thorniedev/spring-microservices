package kh.edu.istad.platform.business.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        BigDecimal price,
        int quantity,
        boolean available
) {
}