package kh.edu.istad.platform.business.port.out;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.entity.Business;
import reactor.core.publisher.Mono;

public interface BusinessRepository {
    Mono<Business> save(Business business);
    Mono<Business> findById(BusinessId businessId);
}