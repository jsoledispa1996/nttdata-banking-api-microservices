package com.nttdata.banking.shared.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter for WebFlux.
 *
 * Extracts JWT token from Authorization header, validates it,
 * and sets Spring Security context.
 *
 * Flow:
 * 1. Extract Bearer token from Authorization header
 * 2. Validate token signature and expiration
 * 3. Extract claims (customerCode, roles, authorities)
 * 4. Create Authentication object
 * 5. Set ReactiveSecurityContextHolder
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Extract token from Authorization header
        String token = extractToken(exchange);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            log.debug("No valid JWT token found for path: {}", path);
            return chain.filter(exchange);
        }

        try {
            // Extract customer code (subject)
            String customerCode = jwtTokenProvider.getSubject(token);

            // Extract claims
            Map<String, Object> claims = jwtTokenProvider.getClaims(token);

            // Extract roles and authorities
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getOrDefault(SecurityConstants.CLAIM_ROLES, List.of());

            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.getOrDefault(SecurityConstants.CLAIM_AUTHORITIES, List.of());

            // Combine roles and authorities
            List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .forEach(grantedAuthorities::add);

            // Create Authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            customerCode,
                            null,
                            grantedAuthorities
                    );

            // Add claims as details
            authentication.setDetails(claims);

            log.debug("Authenticated customer: {} with roles: {} and authorities: {}",
                    customerCode, roles, authorities);

            // Set security context and continue filter chain
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
            return chain.filter(exchange);
        }
    }

    /**
     * Extract JWT token from Authorization header.
     *
     * @param exchange Server web exchange
     * @return JWT token or null
     */
    private String extractToken(ServerWebExchange exchange) {
        String authorization = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization != null && authorization.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return authorization.substring(SecurityConstants.BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Check if path is a public endpoint.
     *
     * @param path Request path
     * @return true if public endpoint
     */
    private boolean isPublicEndpoint(String path) {
        for (String pattern : SecurityConstants.PUBLIC_ENDPOINTS) {
            String regex = pattern.replace("**", ".*").replace("*", "[^/]*");
            if (path.matches(regex)) {
                return true;
            }
        }
        return false;
    }
}
