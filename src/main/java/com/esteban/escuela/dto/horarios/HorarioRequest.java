package com.esteban.escuela.dto.horarios;

import com.esteban.escuela.enums.DiaSemana;

public record HorarioRequest(
        Long idGrupo,
        DiaSemana diaSemana,
        String horaInicio,
        String horaFin
) {
}
