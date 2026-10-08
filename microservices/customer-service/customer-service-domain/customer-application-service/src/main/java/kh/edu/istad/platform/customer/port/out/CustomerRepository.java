package kh.edu.istad.platform.customer.port.out;

import kh.edu.istad.platform.customer.entity.Customer;

// Domain Repository Pattern (DDD), not JAP repository
public interface CustomerRepository
{
    Customer save(Customer customer);
}
