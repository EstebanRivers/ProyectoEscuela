package com.esteban.escuela.services.horarios;

import com.esteban.escuela.dto.horarios.HorarioRequest;
import com.esteban.escuela.dto.horarios.HorarioResponse;
import com.esteban.escuela.entities.Grupo;
import com.esteban.escuela.entities.Horario;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.HorarioMapper;
import com.esteban.escuela.repositories.GrupoRepository;
import com.esteban.escuela.repositories.HorarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HorarioServiceImpl implements HorarioService{
    private final HorarioRepository horarioRepository;
    private final HorarioMapper horarioMapper;
    private final GrupoRepository grupoRepository;


    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponse> listar() {
        log.info("Listando todos los Horarios");
        return horarioRepository.findAll().stream()
                .map(horarioMapper::entityToResponse).toList();
    }

    @Override
    public HorarioResponse obtenerPorId(Long id) {
        return horarioMapper.entityToResponse(obtenerHorarioOException(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        log.info("Registrando horario {}", request);

        validarHoras(request.horaInicio(), request.horaFin());
        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontrado("Grupo no encontrado con el ID: " + request.idGrupo()));

        validarTraslape(request.idGrupo(), grupo.getAula().getId(), request.horaInicio(), request.horaFin());

        Horario horario = horarioMapper.requestToEntity(request);
        horario.setGrupo(grupo);
        Horario horarioGuardado = horarioRepository.save(horario);

        return horarioMapper.entityToResponse(horarioGuardado);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {

        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    private Horario obtenerHorarioOException(Long id) {
        log.debug("Buscando Horario por ID {}", id);
        return horarioRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Horario no encontrado con el id: " + id));
    }

    private void horarioUnico (Long idGrupo, String horaInicio, String horaFin) {
        if (horarioRepository.existsByGrupoIdAndHoraInicioAndHoraFin(idGrupo, horaInicio, horaFin)) {
            throw new IllegalArgumentException("Horario ya existente en ese mismo grupo");
        }

    }

    private void validarHoras(String horaInicio, String horaFin) {
        LocalTime inicio = parseHora(horaInicio);
        LocalTime fin = parseHora(horaFin);

        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("La hora final debe ser posterior a la hora de inicio");
        }
    }

    private void validarTraslape(Long grupoId, Long aulaId, String horaInicio, String horaFin) {
        LocalTime inicio = parseHora(horaInicio);
        LocalTime fin = parseHora(horaFin);

        List<Horario> horariosExistentes = horarioRepository.findByGrupoIdOrGrupoAulaId(grupoId, aulaId);

        for (Horario h : horariosExistentes) {
            LocalTime inicioExistente = parseHora(h.getHoraInicio());
            LocalTime finExistente = parseHora(h.getHoraFin());

            boolean traslape = !(fin.isBefore(inicioExistente) || inicio.isAfter(finExistente));
            if (traslape) {
                throw new IllegalArgumentException("El horario se traslapa con otro existente en el mismo grupo o aula");
            }
        }
    }

    private LocalTime parseHora(String hora) {
        try {
            return LocalTime.parse(hora);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido, debe ser HH:mm");
        }
    }

}
