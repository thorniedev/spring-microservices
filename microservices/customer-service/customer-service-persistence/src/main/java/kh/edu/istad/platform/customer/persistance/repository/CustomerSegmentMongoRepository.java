package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.persistance.document.CustomerSegmentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerSegmentMongoRepository extends ReactiveMongoRepository<CustomerSegmentDocument, String> {

}
