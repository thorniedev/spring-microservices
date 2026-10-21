package kh.edu.istad.authorizationserver.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class ClientAndUserConfig {

    @Bean
    public CommandLineRunner initDatabaseClients(RegisteredClientRepository clientRepository,
                                                PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Public Client with PKCE (for SPAs, React, Angular, Mobile - Keycloak style)
            if (clientRepository.findByClientId("public-client") == null) {
                RegisteredClient publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("public-client")
                        .clientName("Public SPA Client")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://127.0.0.1:3000/callback")
                        .redirectUri("https://oauth.pstmn.io/v1/callback") // Postman callback
                        .postLogoutRedirectUri("http://127.0.0.1:3000")
                        .scope(OidcScopes.OPENID)
                        .scope(OidcScopes.PROFILE)
                        .scope("read")
                        .scope("write")
                        .clientSettings(ClientSettings.builder()
                                .requireProofKey(true) // Enforces PKCE!
                                .requireAuthorizationConsent(false) // Direct redirect without consent page (like Keycloak)
                                .build())
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                                .accessTokenTimeToLive(Duration.ofMinutes(15))
                                .refreshTokenTimeToLive(Duration.ofDays(7))
                                .reuseRefreshTokens(false)
                                .build())
                        .build();

                clientRepository.save(publicClient);
            }

            // 2. Confidential Client (with Secret + PKCE support, for API Gateway / Backend Services)
            if (clientRepository.findByClientId("gateway-client") == null) {
                RegisteredClient gatewayClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("gateway-client")
                        .clientName("API Gateway Client")
                        .clientSecret(passwordEncoder.encode("secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/gateway")
                        .redirectUri("https://oauth.pstmn.io/v1/callback")
                        .postLogoutRedirectUri("http://127.0.0.1:8080")
                        .scope(OidcScopes.OPENID)
                        .scope(OidcScopes.PROFILE)
                        .scope("api.read")
                        .scope("api.write")
                        .clientSettings(ClientSettings.builder()
                                .requireProofKey(true) // Enforces PKCE
                                .requireAuthorizationConsent(false)
                                .build())
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                                .accessTokenTimeToLive(Duration.ofHours(1))
                                .refreshTokenTimeToLive(Duration.ofDays(14))
                                .reuseRefreshTokens(false)
                                .build())
                        .build();

                clientRepository.save(gatewayClient);
            }
        };
    }
}
