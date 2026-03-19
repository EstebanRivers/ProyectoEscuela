package com.esteban.escuela.dto.calificaiones;

import com.esteban.escuela.entities.Calificacion;

import java.math.BigDecimal;

public record CalificacionRequest(
        Long idInscripcion,
        BigDecimal calificacion
) {
}
