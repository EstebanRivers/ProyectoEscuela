package com.esteban.escuela.dto.horarios;

import com.esteban.escuela.enums.DiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

public record HorarioRequest(
        @NotNull(message = "El ID del grupo es requerido")
        Long idGrupo,

        @NotNull(message = "El día de la semana es requerido")
        DiaSemana dia,

        @NotBlank(message = "La hora de inicio es requerida")
        @Positive(message = "La hora inicio no puede ser negativa")
        String horaInicio,

        @NotBlank(message = "La hora de fin es requerida")
        @Positive(message = "La hora final no puede ser negativa")
        String horaFin
) {
}
