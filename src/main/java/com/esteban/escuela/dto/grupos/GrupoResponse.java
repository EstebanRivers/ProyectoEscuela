package com.esteban.escuela.dto.grupos;

import com.esteban.escuela.dto.datos.DatosAula;
import com.esteban.escuela.dto.datos.DatosCurso;
import com.esteban.escuela.dto.datos.DatosMaestro;

public record GrupoResponse(
        Long id,
        DatosCurso curso,
        DatosMaestro maestro,
        DatosAula aula,
        String periodo
        //todavia no estan los dtos de horarios

) {
}
