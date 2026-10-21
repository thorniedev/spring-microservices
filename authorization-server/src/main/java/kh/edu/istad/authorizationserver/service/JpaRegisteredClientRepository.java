package kh.edu.istad.authorizationserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import kh.edu.istad.authorizationserver.entity.Client;
import kh.edu.istad.authorizationserver.repository.ClientEntityRepository;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientEntityRepository clientRepository;
    private final ObjectMapper objectMapper;

    public JpaRegisteredClientRepository(ClientEntityRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.objectMapper = new ObjectMapper();
        ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Client entity = toEntity(registeredClient);
        clientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(Client entity) {
        Set<String> clientAuthMethods = StringUtils.commaDelimitedListToSet(entity.getClientAuthenticationMethods());
        Set<String> grantTypes = StringUtils.commaDelimitedListToSet(entity.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(entity.getRedirectUris());
        Set<String> scopes = StringUtils.commaDelimitedListToSet(entity.getScopes());

        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName())
                .clientAuthenticationMethods(methods ->
                        clientAuthMethods.forEach(m -> methods.add(new ClientAuthenticationMethod(m))))
                .authorizationGrantTypes(types ->
                        grantTypes.forEach(g -> types.add(new AuthorizationGrantType(g))))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .scopes(s -> s.addAll(scopes));

        Map<String, Object> clientSettingsMap = parseMap(entity.getClientSettings());
        builder.clientSettings(clientSettingsMap.isEmpty()
                ? ClientSettings.builder().build()
                : ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = parseMap(entity.getTokenSettings());
        builder.tokenSettings(tokenSettingsMap.isEmpty()
                ? TokenSettings.builder().build()
                : TokenSettings.withSettings(tokenSettingsMap).build());

        return builder.build();
    }

    private Client toEntity(RegisteredClient client) {
        Client entity = new Client();
        entity.setId(client.getId());
        entity.setClientId(client.getClientId());
        entity.setClientIdIssuedAt(client.getClientIdIssuedAt());
        entity.setClientSecret(client.getClientSecret());
        entity.setClientSecretExpiresAt(client.getClientSecretExpiresAt());
        entity.setClientName(client.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(
                client.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).toList()));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(
                client.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList()));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(client.getRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(client.getScopes()));
        entity.setClientSettings(writeMap(client.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(client.getTokenSettings().getSettings()));
        return entity;
    }

    private Map<String, Object> parseMap(String data) {
        try {
            if (StringUtils.hasText(data)) {
                return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
            }
        } catch (Exception ignored) {
        }
        return Collections.emptyMap();
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            return "{}";
        }
    }
}
