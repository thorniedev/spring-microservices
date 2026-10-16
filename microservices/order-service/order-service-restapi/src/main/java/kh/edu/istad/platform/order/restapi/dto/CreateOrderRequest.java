package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull(message = "Customer ID is required")
        UUID customerId,
        @NotNull(message = "Business ID is required")
        UUID businessId,
        @NotNull(message = "Total price is required")
        @Positive(message = "Total price must be greater than zero")
        BigDecimal price,
        @NotEmpty(message = "Items cannot be empty")
        @Valid
        List<OrderItemRequest> items,
        @NotNull(message = "Address is required")
        @Valid
        OrderAddressRequest address
) {
}
