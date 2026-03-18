package com.esteban.escuela.dto.aulas;

public record AulaResponse(
        Long id,
        String nombre,
        Integer capacidad
) {
}
