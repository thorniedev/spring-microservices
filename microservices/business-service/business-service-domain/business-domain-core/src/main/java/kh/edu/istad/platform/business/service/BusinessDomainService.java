package kh.edu.istad.platform.business.service;

import kh.edu.istad.platform.business.entity.Business;
import kh.edu.istad.platform.business.entity.OrderDetail;
import kh.edu.istad.platform.business.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.event.OrderApprovalEvent;

import java.util.List;

public interface BusinessDomainService {
    BusinessRegisteredEvent validateAndInitiateBusiness(Business business);
    OrderApprovalEvent validateOrder(Business business, OrderDetail orderDetail, List<String> failureMessages);
}