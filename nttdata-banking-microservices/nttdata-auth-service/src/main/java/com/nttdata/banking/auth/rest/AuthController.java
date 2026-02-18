package com.nttdata.banking.auth.rest;

import com.nttdata.banking.auth.rest.dto.LoginRequest;
import com.nttdata.banking.auth.rest.dto.LoginResponse;
import com.nttdata.banking.shared.security.JwtTokenProvider;
import com.nttdata.banking.shared.security.SecurityConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Login endpoint - OAuth2 Resource Owner Password Flow.
     *
     * Authenticates user with hardcoded credentials and returns JWT access token.
     *
     * Valid credentials:
     * - username: "admin", password: "admin" → ROLE_ADMIN (full access)
     * - username: "user", password: "user" → ROLE_USER (read-only)

     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());

        // Validate credentials (hardcoded for demo)
        // admin/admin → ROLE_ADMIN (full access)
        // user/user → ROLE_USER (read-only)
        boolean validCredentials =
                ("admin".equals(request.getUsername()) && "admin".equals(request.getPassword())) ||
                        ("user".equals(request.getUsername()) && "user".equals(request.getPassword()));

        if (!validCredentials) {
            log.warn("Credenciales inválidas para usuario: {}", request.getUsername());
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        // Assign roles and authorities based on username
        List<String> roles;
        List<String> authorities;

        if ("admin".equals(request.getUsername())) {
            // ADMIN: Full access (GET, POST, PUT, DELETE)
            roles = List.of(SecurityConstants.ROLE_ADMIN);
            authorities = List.of(
                    SecurityConstants.READ_CUSTOMERS,
                    SecurityConstants.WRITE_CUSTOMERS,
                    SecurityConstants.READ_ACCOUNTS,
                    SecurityConstants.WRITE_ACCOUNTS,
                    SecurityConstants.READ_MOVEMENTS,
                    SecurityConstants.WRITE_MOVEMENTS,
                    SecurityConstants.READ_REPORTS
            );
            log.info("Login de administrador exitoso");
        } else {
            // USER: Read-only access (GET only)
            roles = List.of(SecurityConstants.ROLE_USER);
            authorities = List.of(
                    SecurityConstants.READ_CUSTOMERS,
                    SecurityConstants.READ_ACCOUNTS,
                    SecurityConstants.READ_MOVEMENTS,
                    SecurityConstants.READ_REPORTS
            );
            log.info("Login de usuario exitoso");
        }

        // Create JWT claims
        Map<String, Object> claims = Map.of(
                SecurityConstants.CLAIM_CUSTOMER_CODE, request.getUsername(),
                SecurityConstants.CLAIM_ROLES, roles,
                SecurityConstants.CLAIM_AUTHORITIES, authorities,
                "name", request.getUsername(),
                "active", true
        );

        // Generate JWT token
        String token = jwtTokenProvider.createToken(request.getUsername(), claims);

        // Build response
        LoginResponse response = LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getValidityInSeconds())
                .customerCode(request.getUsername())
                .name(request.getUsername())
                .roles(roles)
                .authorities(authorities)
                .build();

        log.info("Login exitoso para usuario: {}", request.getUsername());
        return Mono.just(ResponseEntity.ok(response));
    }

    /**
     * Token validation endpoint.
     *
     * Validates JWT token and returns decoded claims if valid.
     */
    @GetMapping("/validate")
    public Mono<ResponseEntity<Map<String, Object>>> validateToken(
            @RequestHeader(SecurityConstants.AUTHORIZATION_HEADER) String authorization) {

        String token = authorization.replace(SecurityConstants.BEARER_PREFIX, "");

        if (jwtTokenProvider.validateToken(token)) {
            Map<String, Object> response = Map.of(
                    "valid", true,
                    "subject", jwtTokenProvider.getSubject(token),
                    "claims", jwtTokenProvider.getClaims(token)
            );
            return Mono.just(ResponseEntity.ok(response));
        }

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
