package kh.edu.istad.authorizationserver.config;

import kh.edu.istad.authorizationserver.security.SecurityUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
public class UserSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication principal = context.getPrincipal();

                // Extract custom fields if the principal is our custom SecurityUser
                if (principal.getPrincipal() instanceof SecurityUser securityUser) {
                    context.getClaims().claim("user_id", securityUser.getId().toString());
                    context.getClaims().claim("email", securityUser.getEmail());
                }

                // Add granted roles
                context.getClaims().claim("roles", principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());
            }
        };
    }
}
