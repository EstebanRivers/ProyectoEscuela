package com.esteban.escuela.dto.inscripciones;

import com.esteban.escuela.dto.datos.DatosAlumno;
import com.esteban.escuela.dto.datos.GrupoResumen;
import com.esteban.escuela.entities.Calificacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InscripcionResponse(
        Long id,
        DatosAlumno alumno,
        GrupoResumen grupo,
        BigDecimal calificacion,
        String fechaInscripcion
) {
}
