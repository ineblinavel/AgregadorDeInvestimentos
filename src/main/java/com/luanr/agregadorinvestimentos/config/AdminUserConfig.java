package com.luanr.agregadorinvestimentos.config;

import com.luanr.agregadorinvestimentos.entity.Role;
import com.luanr.agregadorinvestimentos.entity.User;
import com.luanr.agregadorinvestimentos.repository.RoleRepository;
import com.luanr.agregadorinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Configuration
public class AdminUserConfig implements CommandLineRunner { 

    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserRepository userRepository;

    private final String ADMIN_USER = "admin";

    private final String ADMIN_PASSWORD = "adminpass";

    private final String ADMIN_EMAIL = "admin@example";

    public AdminUserConfig(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (ADMIN_USER == null || ADMIN_PASSWORD == null || ADMIN_EMAIL == null) {
            throw new IllegalStateException("One or more required environment variables are not set.");
        }

        var adminRole = roleRepository.findByRoleName(Role.Values.ADMIN.name()).orElseThrow(() ->
                new IllegalStateException("Role ADMIN not found")
        );

        var userAdmin = userRepository.findByUsername(ADMIN_USER);


        userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("User admin already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername(ADMIN_USER);
                    user.setPassword(bCryptPasswordEncoder.encode(ADMIN_PASSWORD));
                    user.setRoles(Set.of(adminRole));
                    user.setUser_id(UUID.randomUUID());
                    user.setEmail(ADMIN_EMAIL);
                    user.setCreated_at(Instant.now());
                    userRepository.save(user);
                }
        );

    }
}
