package com.esteban.escuela.dto.alumnos;

import com.esteban.escuela.dto.datos.DatosCalificaciones;

import java.math.BigDecimal;
import java.util.List;

public record AlumnoResponse(
        Long id,
        String nombre,
        String email,
        String matricula,
        String fechaIngreso,
        List<DatosCalificaciones> calificaciones,
        BigDecimal promedio
) {
}
