package com.esteban.escuela.dto.grupos;

import com.esteban.escuela.dto.aulas.AulaResponse;
import com.esteban.escuela.dto.datos.DatosAula;
import com.esteban.escuela.dto.datos.DatosCurso;
import com.esteban.escuela.dto.datos.DatosMaestro;
import com.esteban.escuela.dto.maestros.MaestroResponse;

import java.util.List;


public record GrupoResponse(
        Long id,
        DatosCurso curso,
        DatosMaestro maestro,
        DatosAula aula,
        String periodo
        //todavia no estan los dtos de horarios

) {
}
