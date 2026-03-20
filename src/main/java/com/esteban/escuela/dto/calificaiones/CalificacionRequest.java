package com.esteban.escuela.dto.calificaiones;

import com.esteban.escuela.entities.Calificacion;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CalificacionRequest(

        @NotNull(message = "Falta la ID de Inscripción")
        Long idInscripcion,

        @NotNull(message = "La calificación es necesaria")
        BigDecimal calificacion
) {
}
