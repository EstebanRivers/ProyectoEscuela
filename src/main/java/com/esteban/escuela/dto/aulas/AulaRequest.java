package com.esteban.escuela.dto.aulas;

import jakarta.validation.constraints.*;

public record AulaRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 30, message = ("El nombre debe tener entre 5 y 30 caracteres"))
        String nombre,

        @NotNull(message = "La capacidad es requerida")
        @Positive(message = "La capacidad debe ser mayor a 0")
        @Max(value = 30, message = ("El limite de capacidad debe ser menor a 30"))
        Integer capacidad
) {
}
