package com.esteban.escuela.dto.aulas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AulaRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 30, message = ("El nombre debe tener entre 5 y 30 caracteres"))
        String nombre,

        @NotNull(message = "La capacidad es requerida")
        Integer capacidad
) {
}
