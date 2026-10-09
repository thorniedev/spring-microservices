package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface CustomerJpaRepository extends R2dbcRepository<CustomerEntity, UUID> {

}
