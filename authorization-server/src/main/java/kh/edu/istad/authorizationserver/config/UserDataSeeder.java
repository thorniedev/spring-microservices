package kh.edu.istad.authorizationserver.config;

import kh.edu.istad.authorizationserver.entity.Role;
import kh.edu.istad.authorizationserver.entity.User;
import kh.edu.istad.authorizationserver.repository.RoleRepository;
import kh.edu.istad.authorizationserver.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class UserDataSeeder {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository,
                                       RoleRepository roleRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsernameWithRoles("thornie").isEmpty()) {
                Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

                Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

                User user = new User();
                user.setUsername("thornie");
                user.setPassword(passwordEncoder.encode("secret123"));
                user.setEmail("thornie@example.com");
                user.setRoles(Set.of(userRole, adminRole));

                userRepository.save(user);
            }
        };
    }
}
