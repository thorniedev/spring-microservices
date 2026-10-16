package kh.edu.istad.platform.order.service;

import kh.edu.istad.platform.order.entity.Business;
import kh.edu.istad.platform.order.entity.Order;
import kh.edu.istad.platform.order.event.OrderCancelledEvent;
import kh.edu.istad.platform.order.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Business business);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);
}
