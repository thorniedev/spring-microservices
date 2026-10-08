package kh.edu.istad.platform.customer.service;

import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.event.CustomerInitiatedEvent;
import kh.edu.istad.platform.customer.event.CustomerUpdatedEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomerDomainServiceImpl implements CustomerDomainService{

    @Override
    public CustomerInitiatedEvent initiateCustomer(Customer customer) {
        customer.initiateCustomer();
        return new CustomerInitiatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerUpdatedEvent updateCustomer(Customer customer) {
        customer.updateCustomer(customer.getFamilyName(), customer.getGivenName());
        return new CustomerUpdatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerDeactivatedEvent deactivateCustomer(Customer customer) {
        customer.deactivateCustomer();
        return new CustomerDeactivatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
