package com.esteban.escuela.services.inscripciones;

import com.esteban.escuela.dto.inscripciones.InscripcionRequest;
import com.esteban.escuela.dto.inscripciones.InscripcionResponse;
import com.esteban.escuela.entities.*;
import com.esteban.escuela.exceptions.EntidadRelacionadaException;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.InscripcionMapper;
import com.esteban.escuela.repositories.AlumnoRepository;
import com.esteban.escuela.repositories.CalificacionRepository;
import com.esteban.escuela.repositories.GrupoRepository;
import com.esteban.escuela.repositories.InscripcionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class InscripcionServiceImpl implements InscripcionService{
    InscripcionRepository inscripcionRepository;
    InscripcionMapper inscripcionMapper;

    AlumnoRepository  alumnoRepository;
    GrupoRepository grupoRepository;
    CalificacionRepository calificacionRepository;

    @Override
    public List<InscripcionResponse> listar() {
        return inscripcionRepository.findAll().stream()
                .map(inscripcionMapper::entityToResponse).toList();
    }

    @Override
    public InscripcionResponse obtenerPorId(Long id) {
        return inscripcionMapper.entityToResponse(obtenerInscripcionOException(id));
    }

    @Override
    public InscripcionResponse registrar(InscripcionRequest request) {
        log.info("Registrando inscripción {}", request);

        inscripcionUnica(request.idGrupo(),  request.idAlumno());

        Inscripcion inscripcion = inscripcionRepository.save(inscripcionMapper.requestToEntity(request));

        log.info("Inscripción con id: {} registrado", inscripcion.getId());

        return inscripcionMapper.entityToResponse(inscripcion);
    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        Inscripcion inscripcion = obtenerInscripcionOException(id);

        log.info("Actualizando inscripción con id: {} ", id);

        inscripcionUnicaActualizada(request.idGrupo(), request.idAlumno(), id);

        Alumno alumno = alumnoRepository.findById(request.idAlumno())
                .orElseThrow(() -> new RecursoNoEncontrado("Alumno no encontrado con id: " + request.idAlumno()));

        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontrado("Grupo no encontrada con id: " + request.idGrupo()));

        inscripcion.setAlumno(alumno);
        inscripcion.setGrupo(grupo);

        return inscripcionMapper.entityToResponse(inscripcion);
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion inscripcion = obtenerInscripcionOException(id);

        log.info("Eliminando inscripción con id: {}", id);

        if(calificacionRepository.existsByInscripcionId(id)){
            throw new EntidadRelacionadaException("No se puede eliminar la inscripción ya que tiene calificaciones asignados");
        }

        inscripcionRepository.delete(inscripcion);

        log.info("Inscripción con id: {} Eliminado", id);

    }

    private Inscripcion obtenerInscripcionOException(Long id) {
        log.debug("Buscando Inscripción por ID {}", id);
        return inscripcionRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Inscripción no encontrada con el id: " + id));
    }

    private void inscripcionUnica(Long grupoId, Long alumnoId) {
        if (inscripcionRepository.existsByGrupoIdAndAlumnoId(grupoId, alumnoId)) {
            throw new IllegalArgumentException("El alumno ya existe en este grupo.");
        }
    }
    private void inscripcionUnicaActualizada(Long grupoId, Long alumnoId, Long id) {
        if (inscripcionRepository.existsByGrupoIdAndAlumnoIdAndIdNot(grupoId, alumnoId, id)) {
            throw new IllegalArgumentException("El alumno ya existe en este grupo.");
        }
    }
}
