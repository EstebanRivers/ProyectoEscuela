package com.esteban.escuela.mappers;

import com.esteban.escuela.dto.datos.DatosInscripcion;
import com.esteban.escuela.dto.inscripciones.InscripcionRequest;
import com.esteban.escuela.dto.inscripciones.InscripcionResponse;
import com.esteban.escuela.entities.*;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.repositories.AlumnoRepository;
import com.esteban.escuela.repositories.GrupoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class InscripcionMapper implements CommonMapper<InscripcionRequest, InscripcionResponse, Inscripcion>{
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    AlumnoRepository alumnoRepository;
    GrupoRepository grupoRepository;

    AlumnoMapper alumnoMapper;
    GrupoMapper grupoMapper;

    @Override
    public Inscripcion requestToEntity(InscripcionRequest request) {
        if(request == null){return null;}

        Alumno alumno = alumnoRepository.findById(request.idAlumno())
                .orElseThrow(() -> new RecursoNoEncontrado("Alumno no encontrado con id: " + request.idAlumno()));

        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontrado("Grupo no encontrado con id: " + request.idGrupo()));

        return Inscripcion.builder()
                .alumno(alumno)
                .grupo(grupo)
                .fechaInscripcion(LocalDate.now())
                .build();
    }

    @Override
    public InscripcionResponse entityToResponse(Inscripcion entity) {
        if (entity == null){return null;}

        return new InscripcionResponse(
                entity.getId(),
                alumnoMapper.alumnoToDatosAlumno(entity.getAlumno()),
                grupoMapper.grupoToGrupoResumen(entity.getGrupo()),
                entity.getCalificacion()!= null ? entity.getCalificacion().getCalificacion() : null,
                entity.getFechaInscripcion().format(formato)
        );
    }

    public DatosInscripcion resumenInscripcion(Inscripcion inscripcion){
        if (inscripcion == null){return null;}

        return new DatosInscripcion(
                alumnoMapper.alumnoToDatosAlumno(inscripcion.getAlumno()),
                grupoMapper.grupoToGrupoResumen(inscripcion.getGrupo()),
                inscripcion.getFechaInscripcion().format(formato)
        );
    }

}
