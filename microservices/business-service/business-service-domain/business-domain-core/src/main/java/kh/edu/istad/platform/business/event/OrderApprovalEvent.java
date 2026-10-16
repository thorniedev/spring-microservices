package kh.edu.istad.platform.business.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.entity.OrderApproval;
import java.time.ZonedDateTime;
import java.util.List;

public record OrderApprovalEvent(
        OrderApproval orderApproval,
        BusinessId businessId,
        List<String> failureMessages,
        ZonedDateTime createdAt
) implements DomainEvent<OrderApproval> {
}