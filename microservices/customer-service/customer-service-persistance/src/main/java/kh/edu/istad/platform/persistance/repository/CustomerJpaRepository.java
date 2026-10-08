package kh.edu.istad.platform.persistance.repository;

import kh.edu.istad.platform.persistance.enity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}
