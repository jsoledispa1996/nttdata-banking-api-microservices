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
public class CreateCustomerRequest {

    @NotBlank(message = "El código de cliente es requerido")
    @Size(min = 3, max = 20, message = "El código de cliente debe tener entre 3 y 20 caracteres")
    private String customerCode;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "El género es requerido")
    @Pattern(regexp = "^(Masculino|Femenino|Otro|Male|Female|Other)$", message = "El género debe ser Masculino, Femenino u Otro")
    private String gender;

    @NotNull(message = "La edad es requerida")
    @Min(value = 18, message = "La edad debe ser al menos 18")
    @Max(value = 120, message = "La edad no debe exceder 120")
    private Integer age;

    @NotBlank(message = "La identificación es requerida")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "La identificación debe contener solo caracteres alfanuméricos")
    @Size(min = 5, max = 20, message = "La identificación debe tener entre 5 y 20 caracteres")
    private String identification;

    @NotBlank(message = "La dirección es requerida")
    @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
    private String address;

    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "^[0-9]{7,20}$", message = "El teléfono debe contener entre 7 y 20 dígitos")
    private String phone;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 4, max = 50, message = "La contraseña debe tener entre 4 y 50 caracteres")
    private String password;
}
