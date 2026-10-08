package kh.edu.istad.platform.customer.persistance.repository;

import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}
