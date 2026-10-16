package kh.edu.istad.platform.payment.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.payment.valueobject.PaymentId;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class Payment extends AggreateRoot<PaymentId> {
    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    public void validatePayment(List<String> failureMessages) {
        if (price == null || !price.isGreaterThanZero()) {
            failureMessages.add("Total price must be greater than zero!");
        }
    }

    public void initializePayment() {
        super.setId(new PaymentId(UUID.randomUUID()));
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void updateStatus(PaymentStatus status) {
        this.paymentStatus = status;
    }

    private Payment(Builder builder) {
        super.setId(builder.paymentId);
        this.orderId = builder.orderId;
        this.customerId = builder.customerId;
        this.price = builder.price;
        this.paymentStatus = builder.paymentStatus;
        this.createdAt = builder.createdAt;
    }

    public OrderId getOrderId() { return orderId; }
    public CustomerId getCustomerId() { return customerId; }
    public Money getPrice() { return price; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public ZonedDateTime getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private PaymentId paymentId;
        private OrderId orderId;
        private CustomerId customerId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        public Builder paymentId(PaymentId val) { paymentId = val; return this; }
        public Builder orderId(OrderId val) { orderId = val; return this; }
        public Builder customerId(CustomerId val) { customerId = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder paymentStatus(PaymentStatus val) { paymentStatus = val; return this; }
        public Builder createdAt(ZonedDateTime val) { createdAt = val; return this; }
        public Payment build() { return new Payment(this); }
    }
}