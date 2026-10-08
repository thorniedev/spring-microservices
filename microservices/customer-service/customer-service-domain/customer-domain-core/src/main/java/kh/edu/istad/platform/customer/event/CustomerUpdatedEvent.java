package kh.edu.istad.platform.customer.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.customer.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerUpdatedEvent implements DomainEvent<Customer> {
    private final Customer customer;
    private final ZonedDateTime initiatedAt;

    // Getter
    public Customer getCustomer() {
        return customer;
    }
    public ZonedDateTime getInitiatedAt() {
        return initiatedAt;
    }

    // constructor
    public CustomerUpdatedEvent(Customer customer, ZonedDateTime utc) {
        this.customer = customer;
        this.initiatedAt = ZonedDateTime.now();
    }
}
