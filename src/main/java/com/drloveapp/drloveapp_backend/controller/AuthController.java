package com.drloveapp.drloveapp_backend.controller;

import com.drloveapp.drloveapp_backend.model.ERole;
import com.drloveapp.drloveapp_backend.model.Role;
import com.drloveapp.drloveapp_backend.model.User;
import com.drloveapp.drloveapp_backend.repository.RoleRepository;
import com.drloveapp.drloveapp_backend.repository.UserRepository;
import com.drloveapp.drloveapp_backend.request.LoginRequest;
import com.drloveapp.drloveapp_backend.request.SignupRequest;
import com.drloveapp.drloveapp_backend.response.JwtResponse;
import com.drloveapp.drloveapp_backend.response.MessageResponse;
import com.drloveapp.drloveapp_backend.service.UserDetailsImpl;
import com.drloveapp.drloveapp_backend.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Update last login time
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow(
                () -> new RuntimeException("Error: User not found."));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        // Create new user's account
        User user = new User();
        user.setEmail(signUpRequest.getEmail());

        // Mot de passe non-encodé pour la connexion automatique
        String rawPassword = signUpRequest.getPassword();

        // Mot de passe encodé pour la sauvegarde
        user.setPassword(encoder.encode(rawPassword));
        user.setFullName(signUpRequest.getFullName());
        user.setCountry(signUpRequest.getCountry());
        user.setCreatedAt(LocalDateTime.now());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user = userRepository.save(user);

        // Générer le token JWT directement sans ré-authentification
        Long userId = user.getId();

        // Créer une liste de chaînes de rôles à partir des rôles de l'utilisateur
        List<String> userRoles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        // Générer le token JWT directement (utiliser une méthode qui ne nécessite pas d'Authentication)
        String jwt = jwtUtils.generateTokenFromUsername(user.getEmail());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userId,
                user.getEmail(),
                userRoles));
    }
}