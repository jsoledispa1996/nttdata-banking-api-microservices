package com.nttdata.banking.auth.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es requerido")
    @Pattern(regexp = "^(admin|user)$", message = "El nombre de usuario debe ser 'admin' o 'user'")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}