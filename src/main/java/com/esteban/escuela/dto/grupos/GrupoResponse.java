package com.esteban.escuela.dto.grupos;

import com.esteban.escuela.dto.datos.DatosAula;
import com.esteban.escuela.dto.datos.DatosCurso;
import com.esteban.escuela.dto.datos.DatosMaestro;
import com.esteban.escuela.dto.horarios.HorarioResponse;

import java.util.List;

public record GrupoResponse(
        Long id,
        DatosCurso curso,
        DatosMaestro maestro,
        DatosAula aula,
        List<String> horarios,
        String periodo


) {
}
