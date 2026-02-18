package com.nttdata.banking.customer.infrastructure.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El género debe ser Masculino, Femenino u Otro")
    private String gender;

    @Min(value = 18, message = "La edad debe ser al menos 18")
    @Max(value = 120, message = "La edad no debe exceder 120")
    private Integer age;

    @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
    private String address;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "El teléfono debe contener entre 10 y 20 dígitos")
    private String phone;

    @Size(min = 4, max = 50, message = "La contraseña debe tener entre 4 y 50 caracteres")
    private String password;

    private Boolean active;
}