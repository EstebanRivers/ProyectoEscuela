package com.esteban.escuela.mappers;

import com.esteban.escuela.dto.datos.GrupoResumen;
import com.esteban.escuela.dto.grupos.GrupoRequest;
import com.esteban.escuela.dto.grupos.GrupoResponse;
import com.esteban.escuela.entities.Aula;
import com.esteban.escuela.entities.Curso;
import com.esteban.escuela.entities.Grupo;
import com.esteban.escuela.entities.Maestro;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.repositories.AulaRepository;
import com.esteban.escuela.repositories.CursoRepository;
import com.esteban.escuela.repositories.MaestroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GrupoMapper implements CommonMapper<GrupoRequest, GrupoResponse, Grupo>{
    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final AulaRepository aulaRepository;

    private final CursoMapper cursoMapper;
    private final MaestroMapper maestroMapper;
    private final AulaMapper aulaMapper;

    @Override
    public Grupo requestToEntity(GrupoRequest request) {
        if (request == null) {return null;}

        Curso curso = cursoRepository.findById(request.idCurso())
                .orElseThrow(() -> new RecursoNoEncontrado("Curso no encontrado con id: " + request.idCurso()));

        Maestro maestro = maestroRepository.findById(request.idMaestro())
                .orElseThrow(() -> new RecursoNoEncontrado("Maestro no encontrado con id: " + request.idMaestro()));

        Aula aula = aulaRepository.findById(request.idAula())
                .orElseThrow(() -> new RecursoNoEncontrado("Aula no encontrada con id: " + request.idAula()));

        return Grupo.builder()
                .curso(curso)
                .maestro(maestro)
                .aula(aula)
                .periodo(request.periodo())
                .build();
    }

    @Override
    public GrupoResponse entityToResponse(Grupo entity) {
        if (entity==null) {return null;}

        return new GrupoResponse(
                entity.getId(),
                cursoMapper.cursoToDatosCurso(entity.getCurso()),
                maestroMapper.maestroToDatosMaestro(entity.getMaestro()),
                aulaMapper.aulaToDatosAula(entity.getAula()),
                entity.getPeriodo()
        );
    }

    public GrupoResumen grupoToGrupoResumen(Grupo grupo) {
        if (grupo==null) {return null;}
        return new GrupoResumen(
                grupo.getCurso().getNombre(),
                grupo.getMaestro().getNombre(),
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );
    }
}
