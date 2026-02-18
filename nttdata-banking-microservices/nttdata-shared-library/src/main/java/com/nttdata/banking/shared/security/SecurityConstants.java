package com.nttdata.banking.shared.security;

/**
 * Security constants for roles and authorities.
 */
public class SecurityConstants {

    // Roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // JWT Claims
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_CUSTOMER_CODE = "customerCode";

    // Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Authorities/Permissions
    public static final String READ_CUSTOMERS = "read:customers";
    public static final String WRITE_CUSTOMERS = "write:customers";
    public static final String READ_ACCOUNTS = "read:accounts";
    public static final String WRITE_ACCOUNTS = "write:accounts";
    public static final String READ_MOVEMENTS = "read:movements";
    public static final String WRITE_MOVEMENTS = "write:movements";
    public static final String READ_REPORTS = "read:reports";

    // Public endpoints
    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

}
