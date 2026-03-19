package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    boolean existsByGrupoIdAndHoraInicioAndHoraFin(Long grupoId,  String horaInicio, String  horaFin);

    List<Horario> findByGrupoIdOrGrupoAulaId(Long grupoId, Long aulaId);
}
