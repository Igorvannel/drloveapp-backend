package com.drloveapp.drloveapp_backend.config;

import com.drloveapp.drloveapp_backend.model.ERole;
import com.drloveapp.drloveapp_backend.model.Role;
import com.drloveapp.drloveapp_backend.model.User;
import com.drloveapp.drloveapp_backend.repository.RoleRepository;
import com.drloveapp.drloveapp_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialiser les rôles si nécessaire
        initRoles();

        // Créer un admin par défaut si nécessaire
        createDefaultAdmin();
    }

    private void initRoles() {
        // Créer les rôles s'ils n'existent pas
        if (roleRepository.count() == 0) {
            // Create user role with constructor instead of setter
            Role userRole = new Role();
            userRole.setId(null);
            userRole.setName(ERole.ROLE_USER);
            roleRepository.save(userRole);

            // Create admin role
            Role adminRole = new Role();
            adminRole.setId(null);
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);

            System.out.println("Roles initialized successfully");
        }
    }

    private void createDefaultAdmin() {
        // Créer un admin par défaut si aucun utilisateur n'existe
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // À changer en production
            admin.setCountry("Cameroun");
            admin.setFullName("Igor Administrator");
            admin.setCreatedAt(LocalDateTime.now());


            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);

            System.out.println("Default admin account created");
        }
    }
}