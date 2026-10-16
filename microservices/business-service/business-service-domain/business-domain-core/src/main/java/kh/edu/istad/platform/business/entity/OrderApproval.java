package kh.edu.istad.platform.business.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.business.valueobject.OrderApprovalId;

public class OrderApproval extends BaseEntity<OrderApprovalId> {
    private final BusinessId businessId;
    private final OrderId orderId;
    private final OrderApprovalStatus approvalStatus;

    public OrderApproval(OrderApprovalId orderApprovalId, BusinessId businessId, OrderId orderId, OrderApprovalStatus approvalStatus) {
        super.setId(orderApprovalId);
        this.businessId = businessId;
        this.orderId = orderId;
        this.approvalStatus = approvalStatus;
    }

    public BusinessId getBusinessId() { return businessId; }
    public OrderId getOrderId() { return orderId; }
    public OrderApprovalStatus getApprovalStatus() { return approvalStatus; }
}