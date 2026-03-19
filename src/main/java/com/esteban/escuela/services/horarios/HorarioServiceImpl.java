package com.esteban.escuela.services.horarios;

import com.esteban.escuela.dto.horarios.HorarioRequest;
import com.esteban.escuela.dto.horarios.HorarioResponse;
import com.esteban.escuela.entities.Grupo;
import com.esteban.escuela.entities.Horario;
import com.esteban.escuela.enums.DiaSemana;
import com.esteban.escuela.exceptions.EntidadRelacionadaException;
import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import com.esteban.escuela.mappers.HorarioMapper;
import com.esteban.escuela.repositories.GrupoRepository;
import com.esteban.escuela.repositories.HorarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        horarioUnico(request.idGrupo(), request.dia(),request.horaInicio(), request.horaFin());
        validarHoras(request.horaInicio(), request.horaFin());
        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontrado("Grupo no encontrado con el ID: " + request.idGrupo()));

        validarTraslape(request.idGrupo(), grupo.getAula().getId(), request.horaInicio(), request.horaFin(), request.dia());

        Horario horario = horarioMapper.requestToEntity(request);
        horario.setGrupo(grupo);
        Horario horarioGuardado = horarioRepository.save(horario);

        return horarioMapper.entityToResponse(horarioGuardado);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {
        Horario horario = obtenerHorarioOException(id);
        log.info("Actualizando horario {}", request);

        Grupo grupo = grupoRepository.findById(request.idGrupo())
                .orElseThrow(() -> new RecursoNoEncontrado("Grupo no encontrado con el ID: " + request.idGrupo()));

        horarioUnicoActualizado(request.idGrupo(), request.dia(),request.horaInicio(), request.horaFin(), id);
        validarHoras(request.horaInicio(), request.horaFin());
        validarTraslapeActualizado(request.idGrupo(), grupo.getAula().getId(), request.horaInicio(), request.horaFin(), request.dia(), id);

        horario.setGrupo(grupo);
        horario.setDia(request.dia());
        horario.setHoraInicio(request.horaInicio());
        horario.setHoraFin(request.horaFin());

        return horarioMapper.entityToResponse(horario);
    }

    @Override
    public void eliminar(Long id) {
        Horario horario = obtenerHorarioOException(id);

        horarioRepository.delete(horario);
    }

    private Horario obtenerHorarioOException(Long id) {
        log.debug("Buscando Horario por ID {}", id);
        return horarioRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontrado("Horario no encontrado con el id: " + id));
    }

    private void horarioUnico (Long idGrupo, DiaSemana diaSemana, String horaInicio, String horaFin) {
        if (horarioRepository.existsByGrupoIdAndDiaAndHoraInicioAndHoraFin(idGrupo, diaSemana, horaInicio, horaFin)) {
            throw new IllegalArgumentException("Horario ya existente en ese mismo grupo y dia");
        }
    }
    private void horarioUnicoActualizado (Long idGrupo, DiaSemana diaSemana, String horaInicio, String horaFin, Long id) {
        if (horarioRepository.existsByGrupoIdAndDiaAndHoraInicioAndHoraFinAndIdNot(idGrupo, diaSemana, horaInicio, horaFin, id)) {
            throw new IllegalArgumentException("Horario ya existente en ese mismo grupo y dia");
        }
    }

    private void validarHoras(String horaInicio, String horaFin) {
        LocalTime inicio = parseHora(horaInicio);
        LocalTime fin = parseHora(horaFin);

        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("La hora final debe ser posterior a la hora de inicio");
        }
    }

    private void validarTraslape(Long grupoId, Long aulaId, String horaInicio, String horaFin, DiaSemana diaSemana) {
        LocalTime inicio = parseHora(horaInicio);
        LocalTime fin = parseHora(horaFin);

        List<Horario> horariosExistentes = horarioRepository.buscarPorGrupoOAulaYDia(grupoId, aulaId, diaSemana);

        boolean existeTraslape = horariosExistentes.stream().anyMatch(h -> {
            LocalTime inicioExistente = parseHora(h.getHoraInicio());
            LocalTime finExistente = parseHora(h.getHoraFin());

            return inicio.isBefore(finExistente) && fin.isAfter(inicioExistente);
        });

        if (existeTraslape) {throw new IllegalArgumentException(
                    "El horario se traslapa con otro existente en el mismo grupo o aula");
        }
    }

    private void validarTraslapeActualizado(Long grupoId, Long aulaId, String horaInicio, String horaFin, DiaSemana diaSemana, Long id) {
        LocalTime inicio = parseHora(horaInicio);
        LocalTime fin = parseHora(horaFin);

        List<Horario> horariosExistentes = horarioRepository.buscarPorGrupoOAulaYDia(grupoId, aulaId, diaSemana);

        boolean existeTraslape = horariosExistentes.stream()
                .filter(h -> !h.getId().equals(id)).anyMatch(h -> {
            LocalTime inicioExistente = parseHora(h.getHoraInicio());
            LocalTime finExistente = parseHora(h.getHoraFin());

            return inicio.isBefore(finExistente) && fin.isAfter(inicioExistente);
        });

        if (existeTraslape) {throw new IllegalArgumentException(
                "El horario se traslapa con otro existente en el mismo grupo o aula");
        }
    }

    private LocalTime parseHora(String hora) {
        try {
            return LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido, debe ser HH:mm");
        }
    }

}
