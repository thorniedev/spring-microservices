package kh.edu.istad.authorizationserver.repository;

import kh.edu.istad.authorizationserver.entity.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AuthorizationEntityRepository extends JpaRepository<Authorization, String>{
    Optional<Authorization> findByState(String state);

    Optional<Authorization> findByAuthorizationCodeValue(String authorizationCode);

    Optional<Authorization> findByAccessTokenValue(String accessToken);

    Optional<Authorization> findByRefreshTokenValue(String refreshToken);

    Optional<Authorization> findByOidcIdTokenValue(String idToken);

    @Query("SELECT a FROM Authorization a WHERE " +
            "a.state = :token OR " +
            "a.authorizationCodeValue = :token OR " +
            "a.accessTokenValue = :token OR " +
            "a.refreshTokenValue = :token OR " +
            "a.oidcIdTokenValue = :token")
    Optional<Authorization> findByAnyTokenValue(@Param("token") String token);
}
