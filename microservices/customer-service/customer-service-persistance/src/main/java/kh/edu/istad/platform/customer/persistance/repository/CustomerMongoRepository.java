package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerMongoRepository extends ReactiveMongoRepository<CustomerDocument, CustomerId> {

}
