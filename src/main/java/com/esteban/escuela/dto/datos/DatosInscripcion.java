package com.esteban.escuela.dto.datos;

import java.time.LocalDate;

public record DatosInscripcion(
        DatosAlumno alumno,
        GrupoResumen grupo,
        String fechaInscripcion
) {
}
