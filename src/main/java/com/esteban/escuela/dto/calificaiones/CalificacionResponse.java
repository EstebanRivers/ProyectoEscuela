package com.esteban.escuela.dto.calificaiones;


import com.esteban.escuela.dto.datos.DatosInscripcion;
import com.esteban.escuela.entities.Calificacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CalificacionResponse(
        Long id,
        DatosInscripcion inscripcion,
        BigDecimal calificacion,
        String fechaRegistro
) {
}
