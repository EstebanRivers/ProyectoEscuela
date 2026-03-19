package com.esteban.escuela.dto.horarios;

import com.esteban.escuela.dto.datos.GrupoResumen;

public record HorarioResponse(
        Long id,
        GrupoResumen grupo,
        String datosHorario
) {
}
