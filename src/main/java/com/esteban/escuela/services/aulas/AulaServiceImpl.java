package com.esteban.escuela.services.aulas;

import com.esteban.escuela.dto.aulas.AulaRequest;
import com.esteban.escuela.dto.aulas.AulaResponse;
import com.esteban.escuela.entities.Aula;
import com.esteban.escuela.entities.Maestro;
import com.esteban.escuela.exceptions.EntidadRelacionadaException;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.AulaMapper;
import com.esteban.escuela.repositories.AulaRepository;
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
public class AulaServiceImpl implements AulaService{
    AulaRepository aulaRepository;
    AulaMapper aulaMapper;
    GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponse> listar() {
        log.info("Iniciando lista de aulas");
        return aulaRepository.findAll().stream()
                .map(aulaMapper::entityToResponse).toList();
    }

    @Override
    public AulaResponse obtenerPorId(Long id) {
        return aulaMapper.entityToResponse(obtenerAulaOException(id));
    }

    @Override
    public AulaResponse registrar(AulaRequest request) {
        log.info("Registrando Aula: {}", request.nombre());

        validarAulaUnica(request.nombre());

        Aula aula = aulaRepository.save(aulaMapper.requestToEntity(request));
        log.info("Registrado Aula: {}", aula.getNombre());

        return aulaMapper.entityToResponse(aula);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, Long id) {
        Aula aula = obtenerAulaOException(id);

        log.info("Actualizando Aula: {}", request.nombre());
        validarAulaActualizada(request.nombre(), id);

        aula.setNombre(request.nombre());
        aula.setCapacidad(request.capacidad());

        log.info("Actualizando Aula: {}", request.nombre());
        return aulaMapper.entityToResponse(aula);
    }

    @Override
    public void eliminar(Long id) {
        Aula aula = obtenerAulaOException(id);

        log.info("Eliminando aula con id: {}", id);

        if(grupoRepository.existsByAulaId(id)){
            throw new EntidadRelacionadaException("No se puede eliminar al aula ya que tiene grupos asignados");
        }

        aulaRepository.delete(aula);

        log.info("Aula con id: {} Eliminada", id);
    }

    private Aula obtenerAulaOException(Long id) {
        log.debug("Buscando Aula por ID {}", id);
        return aulaRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Aula no encontrado con el id: " + id));
    }

    private void validarAulaUnica(String nombre){
        if (aulaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("La Aula ya existe en el sistema");
        }
    }

    private void validarAulaActualizada(String nombre, Long id) {
        if (aulaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new IllegalArgumentException("La Aula ya existe en el sistema");
        }
    }
}
