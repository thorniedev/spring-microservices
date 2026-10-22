package kh.edu.istad.platform.order.domain.restapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
        @NotNull(message = "Product id is required")
        UUID productId,
        @Positive(message = "Quantity must be greater than zero")
        int quantity,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,
        @NotNull(message = "SubTotal is required")
        @Positive(message = "SubTotal must be positive")
        BigDecimal subTotal
) {
}
