package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.platform.order.entity.Business;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BusinessRepositoryAdapter implements BusinessRepository {
    @Override
    public Optional<Business> findBusinessInformation(Business business) {
        return Optional.of(Business.builder()
                .businessId(business.getId())
                .active(true)
                .products(business.getProducts())
                .build());
    }
}
