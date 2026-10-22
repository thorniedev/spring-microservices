package kh.edu.istad.platform.order.domain.restapi.dto;

import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.util.UUID;

public record CreateOrderResponse(
        UUID orderTrackingId,
        OrderStatus orderStatus,
        String message
) {
}
