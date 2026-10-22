package kh.edu.istad.authorizationserver.repository;

import kh.edu.istad.authorizationserver.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientEntityRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);
}
