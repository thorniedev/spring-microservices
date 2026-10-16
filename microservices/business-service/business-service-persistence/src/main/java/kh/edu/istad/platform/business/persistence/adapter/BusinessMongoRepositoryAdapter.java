package kh.edu.istad.platform.business.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.entity.Business;
import kh.edu.istad.platform.business.persistence.mapper.BusinessDocumentMapper;
import kh.edu.istad.platform.business.persistence.repository.BusinessReactiveMongoRepository;
import kh.edu.istad.platform.business.port.out.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BusinessMongoRepositoryAdapter implements BusinessRepository {

    private final BusinessReactiveMongoRepository businessReactiveMongoRepository;
    private final BusinessDocumentMapper businessDocumentMapper;

    @Override
    public Mono<Business> save(Business business) {
        return Mono.just(businessDocumentMapper.toDocument(business))
                .flatMap(businessReactiveMongoRepository::save)
                .map(businessDocumentMapper::toDomain);
    }

    @Override
    public Mono<Business> findById(BusinessId businessId) {
        return businessReactiveMongoRepository.findById(businessId.getValue())
                .map(businessDocumentMapper::toDomain);
    }
}