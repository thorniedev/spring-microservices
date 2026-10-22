package kh.edu.istad.platform.order.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.execption.OrderDomainException;
import kh.edu.istad.platform.order.valueobject.OrderItemId;
import kh.edu.istad.platform.order.valueobject.StreetAddress;
import kh.edu.istad.platform.order.valueobject.TrackingId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends AggreateRoot<OrderId> {
    private final CustomerId customerId;
    private final BusinessId businessId;
    private final StreetAddress deliveryAddress;
    private final Money price; // Order total price
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;
    private final Long version;

    // ------------------ starts domain logic ------------------ //

    /**
     * Initializing order
     */
    public void initializeOrder() {
        super.setId(new OrderId(UUID.randomUUID()));
        this.trackingId = new TrackingId(UUID.randomUUID());
        this.orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    /**
     * Validating the initial order state
     * */
    private void validateInitialOrder() {
        if (price == null || price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateItemsPrice() {
        if (items == null || items.isEmpty()) {
            throw new OrderDomainException("Order must have at least one item!");
        }
        Money orderItemsTotal = items.stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal();
                })
                .reduce(Money.ZERO, Money::add);
        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.amount()
                    + " is not equal to Order items total: " + orderItemsTotal.amount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().amount() +
                    " is not valid for product " + orderItem.getProduct().getId().getValue());
        }
    }

    private void initializeOrderItems() {
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(UUID.randomUUID()));
        }
    }

    private void updateFailureMessages(List<String> messages) {
        if (this.failureMessages != null && messages != null) {
            this.failureMessages.addAll(messages.stream().filter(msg -> !msg.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = messages != null ? new ArrayList<>(messages) : new ArrayList<>();
        }
    }

    // ------------------ ends domain logic ------------------ //

    // Constructor
    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        businessId = builder.businessId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
        version = builder.version;
    }

    // Getters
    public CustomerId getCustomerId() { return customerId; }
    public BusinessId getBusinessId() { return businessId; }
    public StreetAddress getDeliveryAddress() { return deliveryAddress; }
    public Money getPrice() { return price; }
    public List<OrderItem> getItems() { return items; }
    public TrackingId getTrackingId() { return trackingId; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public List<String> getFailureMessages() { return failureMessages; }
    public Long getVersion() { return version; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private BusinessId businessId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;
        private Long version;

        public Builder orderId(OrderId val) { orderId = val; return this; }
        public Builder customerId(CustomerId val) { customerId = val; return this; }
        public Builder businessId(BusinessId val) { businessId = val; return this; }
        public Builder deliveryAddress(StreetAddress val) { deliveryAddress = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder items(List<OrderItem> val) { items = val; return this; }
        public Builder trackingId(TrackingId val) { trackingId = val; return this; }
        public Builder orderStatus(OrderStatus val) { orderStatus = val; return this; }
        public Builder failureMessages(List<String> val) { failureMessages = val; return this; }
        public Builder version(Long val) { version = val; return this; }
        public Order build() { return new Order(this); }
    }

}
