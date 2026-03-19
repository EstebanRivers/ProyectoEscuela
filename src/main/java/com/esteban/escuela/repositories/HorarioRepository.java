package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Horario;
import com.esteban.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    boolean existByGrupoId (Long grupoId);

    boolean existsByGrupoIdAndDiaAndHoraInicioAndHoraFin(Long grupoId, DiaSemana diaSemana, String horaInicio, String  horaFin);
    boolean existsByGrupoIdAndDiaAndHoraInicioAndHoraFinAndIdNot(Long grupoId, DiaSemana diaSemana, String horaInicio, String  horaFin, Long id);

    List<Horario> findByGrupoIdOrGrupoAulaId(Long grupoId, Long aulaId);
}
