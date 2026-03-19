package com.esteban.escuela.dto.inscripciones;

import jakarta.validation.constraints.NotNull;

public record InscripcionRequest(

        @NotNull(message = "La id del Alumno es requerida")
        Long idAlumno,

        @NotNull(message = "La id del Grupo es requerida")
        Long idGrupo
) {
}
