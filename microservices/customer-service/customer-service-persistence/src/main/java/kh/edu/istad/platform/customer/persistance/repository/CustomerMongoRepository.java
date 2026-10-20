package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerMongoRepository extends ReactiveMongoRepository<CustomerDocument, String>, CustomerMongoRepositoryCustom {

    @Query("{username: {$eq: ?0} }")
    Mono<CustomerDocument> findByUsername(String username);

    Mono<Void> deleteById(String id);
}
