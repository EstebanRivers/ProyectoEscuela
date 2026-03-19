package com.esteban.escuela.services.calificaciones;

import com.esteban.escuela.dto.calificaiones.CalificacionRequest;
import com.esteban.escuela.dto.calificaiones.CalificacionResponse;
import com.esteban.escuela.entities.Calificacion;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.CalificacionMapper;
import com.esteban.escuela.repositories.CalificacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CalificacionServiceImpl implements  CalificacionService {
    CalificacionRepository calificacionRepository;
    CalificacionMapper calificacionMapper;

    @Override
    public List<CalificacionResponse> listar() {
        return calificacionRepository.findAll().stream()
                .map(calificacionMapper::entityToResponse).toList();
    }

    @Override
    public CalificacionResponse obtenerPorId(Long id) {
        return calificacionMapper.entityToResponse(obtenerCalificacionOException(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {
        log.info("Iniciando registro de la calificación: {}", request);

        calificacionUnica(request.idInscripcion());

        Calificacion calificacion = calificacionRepository.save(calificacionMapper.requestToEntity(request));

        log.info("Se registro la calificación: {} con el id: {}",  calificacion,  calificacion.getId());

        return calificacionMapper.entityToResponse(calificacion);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, Long id) {
        Calificacion calificacion = obtenerCalificacionOException(id);

        log.info("Actualizando calificación: {}", calificacion);

        calificacionUnicaActualizada(request.idInscripcion(), id);

        calificacion.setCalificacion(request.calificacion());

        return calificacionMapper.entityToResponse(calificacion);
    }

    @Override
    public void eliminar(Long id) {
        Calificacion calificacion = obtenerCalificacionOException(id);
        calificacionRepository.delete(calificacion);
    }

    private Calificacion obtenerCalificacionOException(Long id) {
        log.error("Buscando calificación con el id: {}", id);
        return calificacionRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Calificación no encontrada con el id: " + id));
    }

    private void calificacionUnica(Long inscripcionId) {
        if (calificacionRepository.existsByInscripcionId(inscripcionId )) {
            throw new IllegalArgumentException("Esta calificación ya esta registrada en esta inscripción");
        }
    }
    private void calificacionUnicaActualizada(Long inscripcionId, Long id) {
        if (calificacionRepository.existsByInscripcionIdAndIdNot(inscripcionId, id )) {
            throw new IllegalArgumentException("Esta calificación ya esta registrada en esta inscripción");
        }
    }
}
