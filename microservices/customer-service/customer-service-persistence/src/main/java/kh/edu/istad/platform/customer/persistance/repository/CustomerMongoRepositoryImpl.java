package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class CustomerMongoRepositoryImpl implements CustomerMongoRepositoryCustom{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<PaginatedResponse<CustomerDocument>> findByDynamicFilter(CustomerFilterCriteria criteria) {
        Sort sort = Sort.by(Sort.Direction.fromString(criteria.direction()), criteria.sortBy());
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        Criteria filterCriteria = buildFilterCriteria(criteria);
        Query contentQuery = new Query(filterCriteria).with(pageable);
        Query countQuery = new Query(filterCriteria);
        Mono<List<CustomerDocument>> contentMono = reactiveMongoTemplate.find(contentQuery, CustomerDocument.class).collectList();
        Mono<Long> countMono = reactiveMongoTemplate.count(countQuery, CustomerDocument.class);
        return Mono.zip(contentMono, countMono)
                .map(tuple -> PaginatedResponse.of(
                        tuple.getT1(),
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        tuple.getT2()
                ));
    }

    private Criteria buildFilterCriteria(CustomerFilterCriteria criteria) {
        List<Criteria> criteriaList = new ArrayList<>();
        // 1. firstName (matches given_name with case-insensitive regex)
        if (criteria.firstName() != null && !criteria.firstName().isBlank()) {
            criteriaList.add(Criteria.where("given_name")
                    .regex(Pattern.quote(criteria.firstName().trim()), "i"));
        }
        // 2. lastName (matches family_name with case-insensitive regex)
        if (criteria.lastName() != null && !criteria.lastName().isBlank()) {
            criteriaList.add(Criteria.where("family_name")
                    .regex(Pattern.quote(criteria.lastName().trim()), "i"));
        }
        // 3. email
        if (criteria.email() != null && !criteria.email().isBlank()) {
            criteriaList.add(Criteria.where("email")
                    .regex(Pattern.quote(criteria.email().trim()), "i"));
        }
        // 4. customerStatus
        if (criteria.customerStatus() != null) {
            criteriaList.add(Criteria.where("status").is(criteria.customerStatus()));
        }
        // 5. totalSpent range
        if (criteria.minTotalSpent() != null && criteria.maxTotalSpent() != null) {
            criteriaList.add(Criteria.where("total_spent")
                    .gte(criteria.minTotalSpent())
                    .lte(criteria.maxTotalSpent()));
        } else if (criteria.minTotalSpent() != null) {
            criteriaList.add(Criteria.where("total_spent").gte(criteria.minTotalSpent()));
        } else if (criteria.maxTotalSpent() != null) {
            criteriaList.add(Criteria.where("total_spent").lte(criteria.maxTotalSpent()));
        }
        return criteriaList.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }

    // AGGREGATIONS
    @Override
    public Flux<CustomerStatusStatistic> aggregateCustomerStatusStatistics() {
        Aggregation aggregation = newAggregation(
                group("status")
                        .count().as("totalCustomers")
                        .sum("total_spent").as("totalSpent")
                        .avg("total_spent").as("averageSpent")
                        .max("total_spent").as("maxSpent")
                        .min("total_spent").as("minSpent"),
                project("totalCustomers", "totalSpent", "averageSpent", "maxSpent", "minSpent")
                        .and("_id").as("customerStatus"),
                sort(Sort.Direction.DESC, "totalCustomers")
        );
        return reactiveMongoTemplate.aggregate(aggregation, "customers", CustomerStatusStatistic.class);
    }

    @Override
    public Flux<CustomerSegmentReport> aggregateCustomerSegmentReport() {
        Aggregation aggregation = newAggregation(
                lookup("customer_segments", "segment_id", "_id", "segmentInfo"),
                unwind("segmentInfo", true),
                group("segmentInfo.segment_type")
                        .count().as("totalCount")
                        .sum("total_spent").as("totalRevenue")
                        .avg("loyalty_points").as("averagePoints")
                        .max("total_spent").as("maxPurchaseAmount")
                        .min("total_spent").as("minPurchaseAmount"),
                project("totalCount", "totalRevenue", "averagePoints", "maxPurchaseAmount", "minPurchaseAmount")
                        .and("_id").as("segmentType"),
                sort(Sort.Direction.DESC, "totalRevenue")
        );
        return reactiveMongoTemplate.aggregate(aggregation, "customers", CustomerSegmentReport.class);
    }


    @Override
    public Flux<CustomerStatusAndSegmentStatistic> aggregateCustomerStatusAndSegmentStatistics() {
        Aggregation aggregation = newAggregation(
                lookup("customer_segments", "segment_id", "_id", "segmentInfo"),
                unwind("segmentInfo", true),
                group("status", "segmentInfo.segment_type")
                        .count().as("totalCustomers")
                        .sum("total_spent").as("totalSpent")
                        .avg("total_spent").as("averageSpent"),
                project("totalCustomers", "totalSpent", "averageSpent")
                        .and("_id.status").as("status")
                        .and("_id.segment_type").as("segmentType"),
                sort(Sort.Direction.DESC, "totalCustomers")
        );
        return reactiveMongoTemplate.aggregate(aggregation, "customers", CustomerStatusAndSegmentStatistic.class);
    }
}
