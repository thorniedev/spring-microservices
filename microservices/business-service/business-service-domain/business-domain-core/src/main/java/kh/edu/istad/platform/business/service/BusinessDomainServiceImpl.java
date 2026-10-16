package kh.edu.istad.platform.business.service;

import kh.edu.istad.common.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.platform.business.entity.Business;
import kh.edu.istad.platform.business.entity.OrderDetail;
import kh.edu.istad.platform.business.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.event.OrderApprovalEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class BusinessDomainServiceImpl implements BusinessDomainService {

    private static final String UTC = "UTC";

    @Override
    public BusinessRegisteredEvent validateAndInitiateBusiness(Business business) {
        business.validateBusiness();
        business.initializeBusiness();
        return new BusinessRegisteredEvent(business, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderApprovalEvent validateOrder(Business business, OrderDetail orderDetail, List<String> failureMessages) {
        business.validateOrder(orderDetail);
        if (failureMessages.isEmpty()) {
            business.constructOrderApproval(OrderApprovalStatus.APPROVED);
        } else {
            business.constructOrderApproval(OrderApprovalStatus.REJECTED);
        }
        return new OrderApprovalEvent(
                business.getOrderApproval(),
                business.getId(),
                failureMessages,
                ZonedDateTime.now(ZoneId.of(UTC))
        );
    }
}