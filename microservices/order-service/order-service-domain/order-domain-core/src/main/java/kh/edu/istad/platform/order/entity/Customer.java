package kh.edu.istad.platform.order.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;

public class Customer extends AggreateRoot<CustomerId> {
    public Customer (CustomerId customerId){
        super.setId(customerId);
    }
}
