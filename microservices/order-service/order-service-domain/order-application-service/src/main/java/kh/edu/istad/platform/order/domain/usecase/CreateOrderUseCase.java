package kh.edu.istad.platform.order.domain.usecase;


import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.entity.Business;
import kh.edu.istad.platform.order.entity.Order;
import kh.edu.istad.platform.order.entity.OrderItem;
import kh.edu.istad.platform.order.entity.Product;
import kh.edu.istad.platform.order.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.execption.OrderDomainException;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.service.OrderDomainService;
import kh.edu.istad.platform.order.valueobject.StreetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;

    @Transactional
    public CreateOrderResult execute(CreateOrderCommand createOrderCommand){
        log.info("Creating order for customer: {} at business: {}",
                createOrderCommand.customerId(), createOrderCommand.businessId());
        // 1. Verify Customer exists (Snapshot)
        customerRepository.findCustomer(new CustomerId(createOrderCommand.customerId()))
                .orElseThrow(() -> new OrderDomainException("Could not find customer with id: " + createOrderCommand.customerId()));
        // 2. Build Business snapshot & query repository
        Business business = buildBusinessSnapshot(createOrderCommand);
        Business businessInformation = businessRepository.findBusinessInformation(business)
                .orElseThrow(() -> new OrderDomainException("Could not find business with id: " + createOrderCommand.businessId()));
        // 3. Map Command to Domain Order
        Order order = buildOrder(createOrderCommand);
        // 4. Domain Logic: Validate & Initiate Order
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, businessInformation);
        // 5. Save order through outbound persistence port
        Order savedOrder = orderRepository.save(orderCreatedEvent.order());
        log.info("Order created successfully with tracking id: {}", savedOrder.getTrackingId().getValue());
        return new CreateOrderResult(
                savedOrder.getTrackingId().getValue(),
                savedOrder.getOrderStatus(),
                "Order created successfully!"
        );
    }

    private Order buildOrder(CreateOrderCommand command) {
        List<OrderItem> items = command.items().stream()
                .map(item -> OrderItem.builder()
                        .product(new Product(new ProductId(item.productId())))
                        .price(new Money(item.price()))
                        .quantity(item.quantity())
                        .subTotal(new Money(item.subTotal()))
                        .build())
                .toList();
        StreetAddress deliveryAddress = new StreetAddress(
                command.address().id() != null ? command.address().id() : UUID.randomUUID(),
                command.address().street(),
                command.address().postalCode(),
                command.address().city()
        );
        return Order.builder()
                .customerId(new CustomerId(command.customerId()))
                .businessId(new BusinessId(command.businessId()))
                .deliveryAddress(deliveryAddress)
                .price(new Money(command.price()))
                .items(items)
                .build();
    }
    private Business buildBusinessSnapshot(CreateOrderCommand command) {
        List<Product> products = command.items().stream()
                .map(item -> new Product(new ProductId(item.productId()), "Product", new Money(item.price())))
                .toList();
        return Business.builder()
                .businessId(new BusinessId(command.businessId()))
                .products(products)
                .build();
    }
}
