package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import kh.edu.istad.platform.customer.dto.CustomerFilterCriteria;
import kh.edu.istad.platform.customer.dto.CustomerSegmentReport;
import kh.edu.istad.platform.customer.dto.CustomerStatusStatistic;
import kh.edu.istad.platform.customer.dto.PaginatedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerMongoRepositoryCustom {

    Mono<PaginatedResponse<CustomerDocument>> findByDynamicFilter(CustomerFilterCriteria criteria);

    Flux<CustomerStatusStatistic> aggregateCustomerStatusStatistics();

    Flux<CustomerSegmentReport> aggregateCustomerSegmentReport();

}
