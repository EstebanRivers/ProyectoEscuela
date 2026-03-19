package com.esteban.escuela.services.alumnos;

import com.esteban.escuela.dto.alumnos.AlumnoRequest;
import com.esteban.escuela.dto.alumnos.AlumnoResponse;
import com.esteban.escuela.entities.Alumno;
import com.esteban.escuela.exceptions.EntidadRelacionadaException;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.AlumnoMapper;
import com.esteban.escuela.repositories.AlumnoRepository;

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
public class AlumnoServiceImpl implements AlumnoServices {
    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper  alumnoMapper;
    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {
        log.debug("Iniciando listar alumnos");
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        return alumnoMapper.entityToResponse(obtenerAlumnoOException(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {
        log.debug("Iniciando registrar alumno: {}", request.nombre());

        alumnoUnico(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());

        String matricula = generarMatricula(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno());

        String email = generarEmail(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno());

        Alumno alumno = alumnoRepository.save(alumnoMapper.requestToEntity(request, matricula, email));
        log.info("Alumno registrado con matricula: {}", matricula);

        return alumnoMapper.entityToResponse(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        Alumno alumno = obtenerAlumnoOException(id);
        log.debug("Iniciando actualizar alumno con id: {}", id);

        alumnoUnicoActualizado(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno(), id);

        if (cambioDatosAlumno(request, alumno)) {
            alumno.setNombre(request.nombre());
            alumno.setApellidoPaterno(request.apellidoPaterno());
            alumno.setApellidoMaterno(request.apellidoMaterno());

            regenerarDatosAcademicosAlumno(alumno, request);
            log.info("Alumno actualizado con id: {}", id);
        }
        return alumnoMapper.entityToResponse(alumno);
    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno = obtenerAlumnoOException(id);
        log.debug("Iniciando eliminar alumno con id: {}", id);

        if (inscripcionRepository.existsById(id)) {
            throw new EntidadRelacionadaException("No se puede eliminar el alumno ya que tiene inscripciones asignadas");
        }

        alumnoRepository.delete(alumno);
        log.info("Alumno eliminado con id: {}", id);
    }

    private Alumno obtenerAlumnoOException(Long id) {
        log.debug("Buscando Alumno por ID {}", id);
        return alumnoRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Alumno no encontrado con el id: " + id));
    }

    private void alumnoUnico(String nombre, String paterno, String materno) {
        if (alumnoRepository.existsByNombreAndApellidoPaternoAndApellidoMaterno(nombre, paterno, materno)) {
            throw new IllegalArgumentException("Ya existe un alumno con este nombre: " + nombre + " " + paterno + " " + materno );
        }
    }
    private void alumnoUnicoActualizado(String nombre, String paterno, String materno, Long id) {
        if (alumnoRepository.existsByNombreAndApellidoPaternoAndApellidoMaternoAndIdNot(nombre, paterno, materno, id)) {
            throw new IllegalArgumentException("Ya existe un alumno con este nombre: " + nombre + " " + paterno + " " + materno );
        }
    }

    private String generarMatricula(String nombre, String apellidoPaterno, String apellidoMaterno) {
        log.info("Generando matricula del alumno: {}", nombre);
        return alumnoRepository.generarMatricula(nombre, apellidoPaterno, apellidoMaterno);
    }

    private String generarEmail(String nombre, String apellidoPaterno, String apellidoMaterno) {
        log.info("Generando email del alumno: {}", nombre);
        return alumnoRepository.generarEmail(nombre, apellidoPaterno, apellidoMaterno);
    }

    private boolean cambioDatosAlumno(AlumnoRequest request, Alumno alumno) {
        log.debug("Buscando alumno: {}", request.nombre());
        return !request.nombre().equalsIgnoreCase(alumno.getNombre()) ||
                !request.apellidoPaterno().equalsIgnoreCase(alumno.getApellidoPaterno()) ||
                !request.apellidoMaterno().equalsIgnoreCase(alumno.getApellidoMaterno());
    }
    private void regenerarDatosAcademicosAlumno(Alumno alumno, AlumnoRequest request) {
        String matricula = generarMatricula(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno());

        String email = generarEmail(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno());

        alumno.setMatricula(matricula);
        alumno.setEmail(email);
    }


}
