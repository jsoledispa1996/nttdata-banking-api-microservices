package com.nttdata.banking.auth.config;

import com.nttdata.banking.shared.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Password encoder using BCrypt algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain configuration.
     * All endpoints are public for Auth Service.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        log.info("Configurando cadena de filtros de seguridad para Auth Service (Authorization Server)");

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // All endpoints are public - no authentication required
                        .pathMatchers(SecurityConstants.PUBLIC_ENDPOINTS).permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.GET, "/auth/validate").permitAll()
                        .anyExchange().permitAll()
                )
                .build();
    }
}
