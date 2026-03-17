package com.esteban.escuela.mappers;

import com.esteban.escuela.dto.alumnos.AlumnoRequest;
import com.esteban.escuela.dto.alumnos.AlumnoResponse;
import com.esteban.escuela.dto.datos.DatosCalificaciones;
import com.esteban.escuela.entities.Alumno;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno> {

    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Alumno requestToEntity(AlumnoRequest request) {
        if (request == null) {return null;}

        return Alumno.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .build();
    }

    public Alumno requestToEntity(AlumnoRequest request, String matricula, String email) {
        if (request == null) {return null;}

        Alumno alumno = requestToEntity(request);
        alumno.setMatricula(matricula);
        alumno.setEmail(email);
        return alumno;
    }

    @Override
    public AlumnoResponse entityToResponse(Alumno entity) {
        if (entity == null) {return null;}

        List<DatosCalificaciones> calificaciones = alumnoToDatosCalificaciones(entity);

        return new AlumnoResponse(
                entity.getId(),
                String.join(" ", entity.getNombre(), entity.getApellidoPaterno(), entity.getApellidoMaterno()),
                entity.getEmail(),
                entity.getMatricula(),
                entity.getFechaIngreso().format(formato),
                calificaciones,
                calificacionesToPromedio(calificaciones)
        );
    }

    private List<DatosCalificaciones> alumnoToDatosCalificaciones(Alumno alumno) {
        if (alumno == null || alumno.getInscripciones() == null) {return null;}

        return alumno.getInscripciones().stream()
                .map(inscripcion -> new DatosCalificaciones(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificacion().getCalificacion() != null
                                ? inscripcion.getCalificacion().getCalificacion() : null
                )).toList();
    }

    private BigDecimal calificacionesToPromedio(List<DatosCalificaciones> calificaciones) {
        if (calificaciones == null || calificaciones.isEmpty()) {return BigDecimal.ZERO;}

        //Filtrar las calificaciones NO NULAS
        List<BigDecimal> calificacionesValidas = calificaciones.stream()
                .map(DatosCalificaciones::calificacion)
                .filter(Objects::nonNull).toList();

        if (calificacionesValidas.isEmpty()) {return BigDecimal.ZERO;}

        BigDecimal suma = calificacionesValidas.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return suma.divide(BigDecimal.valueOf(calificacionesValidas.size()), 2, RoundingMode.HALF_UP);
    }
}
