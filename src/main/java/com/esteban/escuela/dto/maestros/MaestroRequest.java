package com.esteban.escuela.dto.maestros;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MaestroRequest(
        @NotBlank (message = "El nombre es requerido")
        @Size(min = 3, max = 50, message = "EL nombre debe de tener entre 3 y 50 caracteres")
        String nombre,

        @NotBlank (message = "El apellido paterno es requerido")
        @Size(min = 3, max = 50, message = "EL apellido paterno debe de tener entre 3 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank (message = "El apellido materno es requerido")
        @Size(min = 3, max = 50, message = "EL apellido materno debe de tener entre 3 y 50 caracteres")
        String apellidoMaterno,

        @NotBlank (message = "El email es requerido")
        @Size(max = 100, message = "El email debe de tener 100 caracteres")
        @Email(message = "EL email debe tener un formato valido (ejemplo@dominio.com")
        String email,

        @NotBlank (message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener solo 10 dígitos")
        String telefono
) {
}
