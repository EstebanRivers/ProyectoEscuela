package com.esteban.escuela.services.cursos;

import com.esteban.escuela.dto.cursos.CursoRequest;
import com.esteban.escuela.dto.cursos.CursoResponse;
import com.esteban.escuela.entities.Curso;
import com.esteban.escuela.exceptions.EntidadRelacionadaException;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.CursoMapper;
import com.esteban.escuela.repositories.CursoRepository;
import com.esteban.escuela.repositories.GrupoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CursoServiceImpl implements  CursoService {
    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        log.info("Iniciando lista de cursos");
        return cursoRepository.findAll().stream()
                .map(cursoMapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        return cursoMapper.entityToResponse(obtenerCursoOException(id));
    }

    @Override
    public CursoResponse registrar(CursoRequest request) {
        log.info("Registrando Curso: {}", request.nombre());

        validarCursoUnico(request.nombre());

        Curso curso = cursoRepository.save(cursoMapper.requestToEntity(request));
        log.info("Registrado Curso: {}", curso.getNombre());

        return cursoMapper.entityToResponse(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {
        Curso curso = obtenerCursoOException(id);

        log.info("Actualizando Curso: {}", request.nombre());
        validarCursoActualizado(request.nombre(), id);

        curso.setNombre(request.nombre());
        curso.setDescripcion(request.descripcion());
        curso.setCreditos(request.creditos());

        log.info("Actualizando Curso: {}", request.nombre());
        return cursoMapper.entityToResponse(curso);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerCursoOException(id);

        log.info("Eliminando curso con id: {}", id);

        if(grupoRepository.existsByCursoId(id)){
            throw new EntidadRelacionadaException("No se puede eliminar al curso ya que tiene grupos asignados");
        }

        cursoRepository.delete(curso);

        log.info("Curso con id: {} Eliminado", id);
    }

    private Curso obtenerCursoOException(Long id) {
        log.debug("Buscando Curso por ID {}", id);
        return cursoRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Curso no encontrado con el id: " + id));
    }

    private void validarCursoUnico(String nombre){
        if (cursoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("El curso ya existe en el sistema");
        }
    }

    private void validarCursoActualizado(String nombre, Long id) {
        if (cursoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new IllegalArgumentException("El curso ya existe en el sistema");
        }
    }
}
