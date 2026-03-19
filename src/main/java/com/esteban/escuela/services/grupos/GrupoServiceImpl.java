package com.esteban.escuela.services.grupos;

import com.esteban.escuela.dto.grupos.GrupoRequest;
import com.esteban.escuela.dto.grupos.GrupoResponse;
import com.esteban.escuela.entities.Aula;
import com.esteban.escuela.entities.Curso;
import com.esteban.escuela.entities.Grupo;
import com.esteban.escuela.entities.Maestro;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.GrupoMapper;
import com.esteban.escuela.repositories.AulaRepository;
import com.esteban.escuela.repositories.CursoRepository;
import com.esteban.escuela.repositories.GrupoRepository;
import com.esteban.escuela.repositories.MaestroRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class GrupoServiceImpl implements GrupoService {
    private final GrupoRepository grupoRepository;
    private final GrupoMapper grupoMapper;

    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final AulaRepository aulaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        log.info("Iniciando lista de Grupos");
        return grupoRepository.findAll().stream()
                .map(grupoMapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        log.info("Iniciando obtener grupo por id: {}", id);

        return grupoMapper.entityToResponse(obtenerGrupoOException(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {
        log.info("Registrando Grupo: {}", request.idCurso());

        validarCursoMaestroAulaPeriodoUnico(request.idCurso(), request.idMaestro(), request.idAula(), request.periodo());

        Grupo grupo = grupoRepository.save(grupoMapper.requestToEntity(request));
        log.info("Registrado Curso: {}", grupo.getCurso());

        return grupoMapper.entityToResponse(grupo);
    }

    @Override
    @Transactional
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        Grupo grupo = obtenerGrupoOException(id);
        log.info("Actualizando Grupo: {}", grupo.getCurso());

        validarCursoMaestroAulaPeriodoUnicoActualizado(request.idCurso(), request.idMaestro(), request.idAula(), request.periodo(), id);

        Curso curso = cursoRepository.findById(request.idCurso())
                .orElseThrow(() -> new RecursoNoEncontrado("Curso no encontrado con id: " + request.idCurso()));

        Maestro maestro = maestroRepository.findById(request.idMaestro())
                .orElseThrow(() -> new RecursoNoEncontrado("Maestro no encontrado con id: " + request.idMaestro()));

        Aula aula = aulaRepository.findById(request.idAula())
                .orElseThrow(() -> new RecursoNoEncontrado("Aula no encontrada con id: " + request.idAula()));

        grupo.setCurso(curso);
        grupo.setMaestro(maestro);
        grupo.setAula(aula);
        grupo.setPeriodo(request.periodo());

        log.info("Actualizando Grupo: {}", id);
        return grupoMapper.entityToResponse(grupo);
    }

    @Override
    public void eliminar(Long id) {
        Grupo grupo = obtenerGrupoOException(id);
        log.info("Eliminando Grupo: {}", grupo.getCurso());
        grupoRepository.delete(grupo);
        log.info("Grupo Eliminando: {}", id);

    }

    private Grupo obtenerGrupoOException(Long id) {
        log.debug("Buscando Grupo por ID {}", id);
        return grupoRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Grupo no encontrado con el id: " + id));
    }

    private void validarCursoMaestroAulaPeriodoUnico(Long cursoId, Long maestroId, Long aulaId, String periodo) {
        if (grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(cursoId, maestroId, aulaId, periodo)) {
            throw new IllegalArgumentException("Ya existe un grupo con este mismo curso, maestro, aula y periodo");
        }
    }
    private void validarCursoMaestroAulaPeriodoUnicoActualizado(Long cursoId, Long maestroId, Long aulaId, String periodo, Long id) {
        if (grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(cursoId, maestroId, aulaId, periodo, id)) {
            throw new IllegalArgumentException("Ya existe un grupo con este mismo curso, maestro, aula y periodo");
        }
    }
}
