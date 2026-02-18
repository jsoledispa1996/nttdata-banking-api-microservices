package com.nttdata.banking.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;


/**
 * JWT Token Provider - Generates and validates JWT tokens.
 *
 * Features:
 * - Token generation with custom claims
 * - Token validation and parsing
 * - Expiration handling
 * - Secret key management
 */

@Component
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    private final long validityInMilliseconds;
    
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long validityInSeconds) {
        // Create a secure key from the secret
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        // Convert seconds to milliseconds
        this.validityInMilliseconds = validityInSeconds * 1000;
    }
    
    /**
     * Generate JWT token with custom claims.
     */
    public String createToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setIssuer("banking-microservices")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Generate simple token with just subject.
     */
    public String createToken(String subject) {
        return createToken(subject, Map.of());
    }
    
    /**
     * Validate JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extract subject (username/customer code) from token.
     */
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }
    
    /**
     * Extract all claims from token.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Extract custom claim from token.
     */
    public Object getClaim(String token, String claimName) {
        return getClaims(token).get(claimName);
    }
    
    /**
     * Check if token is expired.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * Get token validity in seconds.
     */
    public long getValidityInSeconds() {
        return validityInMilliseconds / 1000;
    }
}
