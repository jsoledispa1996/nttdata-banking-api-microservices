package com.nttdata.banking.customer.infrastructure.rest.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Login response DTO with JWT token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String customerCode;
    private String name;
    private List<String> roles;
    private List<String> authorities;
}
