package kh.edu.istad.platform.order.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.valueobject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {

    private OrderId orderId;
    private final Product product;
    private final Integer quantity;
    private final Money price;
    private final Money subTotal;

    // Constructor
    public OrderItem(Money subTotal, Money price, Integer quantity, Product product, OrderId orderId) {
        this.subTotal = subTotal;
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.orderId = orderId;
    }

    // Getters
    public OrderId getOrderId() { return orderId; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public Money getPrice() { return price; }
    public Money getSubTotal() { return subTotal; }


    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        return price != null &&
                price.isGreaterThanZero() &&
                price.equals(product.getPrice()) &&
                subTotal.equals(price.multiply(quantity));
    }

    // Builder
    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        this.orderId = builder.orderId;
        this.product = builder.product;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.subTotal = builder.subTotal;
    }
    public static Builder builder() { return new Builder(); }
    public static final class Builder {
        
        private OrderItemId orderItemId;
        private OrderId orderId;
        private Product product;
        private Integer quantity;
        private Money price;
        private Money subTotal;

        public Builder orderItemId(OrderItemId val) { orderItemId = val; return this; }
        public Builder orderId(OrderId val) { orderId = val; return this; }
        public Builder product(Product val) { product = val; return this; }
        public Builder quantity(Integer val) { quantity = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder subTotal(Money val) { subTotal = val; return this; }
        public OrderItem build() { return new OrderItem(this); }
    }

}
