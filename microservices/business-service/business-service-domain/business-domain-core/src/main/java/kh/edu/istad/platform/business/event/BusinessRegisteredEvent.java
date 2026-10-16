package kh.edu.istad.platform.business.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.business.entity.Business;
import java.time.ZonedDateTime;

public record BusinessRegisteredEvent(
        Business business,
        ZonedDateTime createdAt
) implements DomainEvent<Business> {
}