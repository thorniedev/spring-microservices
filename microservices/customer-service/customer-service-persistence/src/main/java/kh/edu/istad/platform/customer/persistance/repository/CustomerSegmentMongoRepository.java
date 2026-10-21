package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.persistance.document.CustomerSegmentDocument;
import kh.edu.istad.platform.customer.valueobject.SegmentType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface CustomerSegmentMongoRepository extends ReactiveMongoRepository<CustomerSegmentDocument, String> {
    Flux<CustomerSegmentDocument> findBySegmentType(SegmentType segmentType);
}
