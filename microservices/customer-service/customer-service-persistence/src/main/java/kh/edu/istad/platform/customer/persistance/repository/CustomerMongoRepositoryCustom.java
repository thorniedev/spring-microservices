package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerMongoRepositoryCustom {

    Mono<PaginatedResponse<CustomerDocument>> findByDynamicFilter(CustomerFilterCriteria criteria);

    Flux<CustomerStatusStatistic> aggregateCustomerStatusStatistics();

    Flux<CustomerSegmentReport> aggregateCustomerSegmentReport();

    Flux<CustomerStatusAndSegmentStatistic> aggregateCustomerStatusAndSegmentStatistics();

}
