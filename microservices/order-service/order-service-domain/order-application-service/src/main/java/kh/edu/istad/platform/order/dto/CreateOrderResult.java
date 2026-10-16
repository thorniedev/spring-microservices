package kh.edu.istad.platform.order.dto;

import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.util.UUID;

public record CreateOrderResult(
        UUID orderTrackingId,
        OrderStatus orderStatus,
        String message
) {
}
