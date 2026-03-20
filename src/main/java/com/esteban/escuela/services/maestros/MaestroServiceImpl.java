package com.esteban.escuela.services.maestros;

import com.esteban.escuela.dto.maestros.MaestroRequest;
import com.esteban.escuela.dto.maestros.MaestroResponse;
import com.esteban.escuela.entities.Maestro;
import com.esteban.escuela.exceptions.EntidadRelacionadaException;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.MaestroMapper;
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
public class MaestroServiceImpl implements  MaestroService {
    private final MaestroRepository maestroRepository;
    private final MaestroMapper maestroMapper;

    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listar() {
        log.info("Listando todos los maestros");
        return maestroRepository.findAll().stream()
                .map(maestroMapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.entityToResponse(obtenerMaestroOException(id));
    }

    @Override
    public MaestroResponse registrar(MaestroRequest request) {
        log.info("Registrando maestro: {}", request.nombre());

        validarTelefonoUnico(request.telefono());
        validarEmailUnico(request.email());

        Maestro maestro = maestroRepository.save(maestroMapper.requestToEntity(request));
        log.info("Registrado maestro: {}", maestro.getNombre());

        return maestroMapper.entityToResponse(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        Maestro maestro = obtenerMaestroOException(id);
        log.info("Actualizando maestro con id: {}", id);

        validarCambiosUnicos(request, id);

        maestro.setNombre(request.nombre());
        maestro.setApellidoPaterno(request.apellidoPaterno());
        maestro.setApellidoMaterno(request.apellidoMaterno());
        maestro.setEmail(request.email());
        maestro.setTelefono(request.telefono());

        log.info("Maestro con id: {} Actualizado", id);
        return maestroMapper.entityToResponse(maestro);
    }

    @Override
    public void eliminar(Long id) {
        Maestro maestro = obtenerMaestroOException(id);

        log.info("Eliminando maestro con id: {}", id);

        if(grupoRepository.existsByMaestroId(id)){
            throw new EntidadRelacionadaException("No se puede eliminar al maestro ya qye tiene grupos asignados");
        }

        maestroRepository.delete(maestro);

        log.info("Maestro con id: {} Eliminado", id);
    }

    private Maestro obtenerMaestroOException(Long id) {
        log.info("Obteniendo maestro por id: {}", id);
        return maestroRepository.findById(id).orElseThrow( () ->
                new RecursoNoEncontrado("Maestro no encontrado por id: " + id));
    }

    private void validarEmailUnico(String email) {
        if (maestroRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("El email ya existe en el sistema: " + email);
        }
    }
    private void validarTelefonoUnico(String telefono) {
        if (maestroRepository.existsByTelefono(telefono)) {
            throw new IllegalArgumentException("El telefono ya existe en el sistema: " + telefono);
        }
    }
    private void validarCambiosUnicos(MaestroRequest request, Long id) {
        if (maestroRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new IllegalArgumentException("El email ya existe en el sistema: " + request.email());
        }
        if (maestroRepository.existsByTelefonoAndIdNot(request.telefono(), id)) {
            throw new IllegalArgumentException("El telefono ya existe en el sistema:  " + request.telefono());
        }
    }


}
