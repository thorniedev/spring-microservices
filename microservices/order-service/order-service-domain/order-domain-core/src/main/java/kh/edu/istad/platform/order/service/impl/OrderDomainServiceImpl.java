package kh.edu.istad.platform.order.service.impl;

import kh.edu.istad.platform.order.entity.Business;
import kh.edu.istad.platform.order.entity.Order;
import kh.edu.istad.platform.order.entity.Product;
import kh.edu.istad.platform.order.event.OrderCancelledEvent;
import kh.edu.istad.platform.order.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.event.OrderPaidEvent;
import kh.edu.istad.platform.order.execption.OrderDomainException;
import kh.edu.istad.platform.order.service.OrderDomainService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


public class OrderDomainServiceImpl implements OrderDomainService {

    private final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Business business) {
        validateBusiness(business);
        setOrderProductInformation(order, business);
        order.validateOrder();
        order.initializeOrder();
        // log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        // log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        // log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        // log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        // log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    //
    private void validateBusiness(Business business) {
        if (!business.isActive()) {
            throw new OrderDomainException("Business with id " + business.getId().getValue() +
                    " is currently not active!");
        }
    }
    private void setOrderProductInformation(Order order, Business business) {
        order.getItems().forEach(orderItem -> business.getProducts().forEach(businessProduct -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(businessProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(businessProduct.getName(), businessProduct.getPrice());
            }
        }));
    }
}
