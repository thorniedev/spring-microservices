package kh.edu.istad.platform.order.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.entity.Order;
import kh.edu.istad.platform.order.entity.OrderItem;
import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import kh.edu.istad.platform.order.valueobject.OrderItemId;
import kh.edu.istad.platform.order.valueobject.StreetAddress;
import kh.edu.istad.platform.order.valueobject.TrackingId;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {

    // Domain Order -> JPA OrderEntity
    @Mapping(target = "id", source = "id.value")
    //@Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customerId.value")
    @Mapping(target = "businessId", source = "businessId.value")
    @Mapping(target = "trackingId", source = "trackingId.value")
    @Mapping(target = "price", source = "price.amount")
    @Mapping(target = "failureMessages", expression = "java(order.getFailureMessages() != null ? String.join(\";\", order.getFailureMessages()) : \"\")")
    OrderEntity toEntity(Order order);

    // JPA OrderEntity -> Domain Order
    // @Mapping(target = "id", source = "id")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "businessId", source = "businessId")
    @Mapping(target = "trackingId", source = "trackingId")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "failureMessages", expression = "java(entity.getFailureMessages() != null && !entity.getFailureMessages().isEmpty() ? new java.util.ArrayList<>(java.util.Arrays.asList(entity.getFailureMessages().split(\";\"))) : new java.util.ArrayList<>())")
    Order toDomain(OrderEntity entity);

    // OrderItem mappings
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "productId", source = "product.id.value")
    @Mapping(target = "price", source = "price.amount")
    @Mapping(target = "subTotal", source = "subTotal.amount")
    @Mapping(target = "order", ignore = true)
    OrderItemEntity toItemEntity(OrderItem item);

    // @Mapping(target = "id", source = "id")
    @Mapping(target = "orderItemId", source = "id")
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "product", expression = "java(new Product(new ProductId(itemEntity.getProductId())))")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "subTotal", source = "subTotal")
    OrderItem toItemDomain(OrderItemEntity itemEntity);

    // Address mappings
    OrderAddressEntity toAddressEntity(StreetAddress address);
    StreetAddress toAddressDomain(OrderAddressEntity addressEntity);

    // Ensure bidirectional link between OrderEntity and OrderItemEntity
    @AfterMapping
    default void setOrderInItems(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
    }

    // Type Converters for Value Objects
    default CustomerId mapCustomerId(UUID value) { return value != null ? new CustomerId(value) : null; }
    default BusinessId mapBusinessId(UUID value) { return value != null ? new BusinessId(value) : null; }
    default OrderId mapOrderId(UUID value) { return value != null ? new OrderId(value) : null; }
    default TrackingId mapTrackingId(UUID value) { return value != null ? new TrackingId(value) : null; }
    default OrderItemId mapOrderItemId(UUID value) { return value != null ? new OrderItemId(value) : null; }
    default Money mapMoney(BigDecimal value) { return value != null ? new Money(value) : null; }

}










//@Component
//public class OrderPersistenceMapper {
//
//
//    public OrderEntity toEntity(Order order) {
//        OrderAddressEntity addressEntity = OrderAddressEntity.builder()
//                .id(order.getDeliveryAddress().id())
//                .street(order.getDeliveryAddress().street())
//                .postalCode(order.getDeliveryAddress().postalCode())
//                .city(order.getDeliveryAddress().city())
//                .build();
//        OrderEntity orderEntity = OrderEntity.builder()
//                .id(order.getId().getValue())
//                .customerId(order.getCustomerId().getValue())
//                .businessId(order.getBusinessId().getValue())
//                .trackingId(order.getTrackingId().getValue())
//                .price(order.getPrice().amount())
//                .orderStatus(order.getOrderStatus())
//                .failureMessages(order.getFailureMessages() != null ? String.join(";", order.getFailureMessages()) : "")
//                .deliveryAddress(addressEntity)
//                .build();
//        List<OrderItemEntity> itemEntities = order.getItems().stream()
//                .map(item -> OrderItemEntity.builder()
//                        .id(item.getId().getValue())
//                        .order(orderEntity)
//                        .productId(item.getProduct().getId().getValue())
//                        .price(item.getPrice().amount())
//                        .quantity(item.getQuantity())
//                        .subTotal(item.getSubTotal().amount())
//                        .build())
//                .toList();
//        orderEntity.setItems(new ArrayList<>(itemEntities));
//        return orderEntity;
//    }
//
//    public Order toDomain(OrderEntity entity) {
//        StreetAddress address = new StreetAddress(
//                entity.getDeliveryAddress().getId(),
//                entity.getDeliveryAddress().getStreet(),
//                entity.getDeliveryAddress().getPostalCode(),
//                entity.getDeliveryAddress().getCity()
//        );
//        List<OrderItem> items = entity.getItems().stream()
//                .map(itemEntity -> OrderItem.builder()
//                        .orderItemId(new OrderItemId(itemEntity.getId()))
//                        .orderId(new OrderId(entity.getId()))
//                        .product(new Product(new ProductId(itemEntity.getProductId())))
//                        .price(new Money(itemEntity.getPrice()))
//                        .quantity(itemEntity.getQuantity())
//                        .subTotal(new Money(itemEntity.getSubTotal()))
//                        .build())
//                .toList();
//        return Order.builder()
//                .orderId(new OrderId(entity.getId()))
//                .customerId(new CustomerId(entity.getCustomerId()))
//                .businessId(new BusinessId(entity.getBusinessId()))
//                .deliveryAddress(address)
//                .price(new Money(entity.getPrice()))
//                .items(items)
//                .trackingId(new TrackingId(entity.getTrackingId()))
//                .orderStatus(entity.getOrderStatus())
//                .failureMessages(entity.getFailureMessages() != null && !entity.getFailureMessages().isEmpty()
//                        ? new ArrayList<>(Arrays.asList(entity.getFailureMessages().split(";")))
//                        : new ArrayList<>())
//                .build();
//    }
//
//}
