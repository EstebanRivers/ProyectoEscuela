package com.esteban.escuela.mappers;

import com.esteban.escuela.dto.calificaiones.CalificacionRequest;
import com.esteban.escuela.dto.calificaiones.CalificacionResponse;
import com.esteban.escuela.entities.Calificacion;
import com.esteban.escuela.entities.Inscripcion;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.repositories.InscripcionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class CalificacionMapper implements CommonMapper<CalificacionRequest, CalificacionResponse, Calificacion> {
    InscripcionMapper inscripcionMapper;
    InscripcionRepository inscripcionRepository;

    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Calificacion requestToEntity(CalificacionRequest request) {
        if (request==null) {return null;}

        Inscripcion inscripcion = inscripcionRepository.findById(request.idInscripcion())
                .orElseThrow(() -> new RecursoNoEncontrado("Inscripción no encontrado con id: " + request.idInscripcion()));

        return Calificacion.builder()
                .inscripcion(inscripcion)
                .calificacion(request.calificacion())
                .fechaRegistro(LocalDate.now())
                .build();
    }

    @Override
    public CalificacionResponse entityToResponse(Calificacion entity) {
        if (entity==null) {return null;}

        return new CalificacionResponse(
                entity.getId(),
                inscripcionMapper.resumenInscripcion(entity.getInscripcion()),
                entity.getCalificacion(),
                entity.getFechaRegistro().format(formato)
        );
    }
}
