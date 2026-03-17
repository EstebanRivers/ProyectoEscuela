package com.esteban.escuela.dto.alumnos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlumnoRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 50, message = "EL nombre debe de tener entre 3 y 50 caracteres")
        String nombre,

        @NotBlank (message = "El apellido paterno es requerido")
        @Size(min = 3, max = 50, message = "EL apellido paterno debe de tener entre 3 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank (message = "El apellido materno es requerido")
        @Size(min = 3, max = 50, message = "EL apellido materno debe de tener entre 3 y 50 caracteres")
        String apellidoMaterno

) {
}
