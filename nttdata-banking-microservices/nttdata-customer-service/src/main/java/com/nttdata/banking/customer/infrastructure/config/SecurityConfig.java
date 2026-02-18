package com.nttdata.banking.customer.infrastructure.config;

import com.nttdata.banking.shared.security.JwtAuthenticationFilter;
import com.nttdata.banking.shared.security.JwtTokenProvider;
import com.nttdata.banking.shared.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security Configuration for Customer Service.
 * 
 * Role: Resource Server ONLY
 * - Validates JWT tokens for protected endpoints
 * - Does NOT generate tokens (handled by auth-service)
 * 
 * Public endpoints:
 * - /actuator/** (health, metrics)
 * 
 * Protected endpoints:
 * - /api/v1/customers/** (requires valid JWT)
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    
    /**
     * Password encoder using BCrypt algorithm.
     * 
     * @return BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Security filter chain configuration.
     * 
     * @param http ServerHttpSecurity
     * @param jwtTokenProvider JWT token provider
     * @return Security web filter chain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtTokenProvider jwtTokenProvider) {
        log.info("Configurando cadena de filtros de seguridad para Customer Service (Resource Server)");
        
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Public endpoints - no authentication required
                        .pathMatchers(SecurityConstants.PUBLIC_ENDPOINTS).permitAll()
                        
                        // Customer endpoints - role-based access
                        .pathMatchers(HttpMethod.GET, "/api/v1/customers/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/customers/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/customers/**").hasRole("ADMIN")
                        
                        // All other endpoints require authentication
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
