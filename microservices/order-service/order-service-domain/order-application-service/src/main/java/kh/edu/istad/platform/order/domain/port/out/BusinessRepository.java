package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.platform.order.entity.Business;

import java.util.Optional;

public interface BusinessRepository {
    Optional<Business> findBusinessInformation(Business business);
}
