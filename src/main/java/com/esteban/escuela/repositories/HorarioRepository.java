package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Horario;
import com.esteban.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    boolean existsByGrupoId (Long grupoId);

    boolean existsByGrupoIdAndDiaAndHoraInicioAndHoraFin(Long grupoId, DiaSemana diaSemana, String horaInicio, String  horaFin);
    boolean existsByGrupoIdAndDiaAndHoraInicioAndHoraFinAndIdNot(Long grupoId, DiaSemana diaSemana, String horaInicio, String  horaFin, Long id);

    @Query("SELECT h FROM Horario h " +
            "JOIN h.grupo g " +
            "JOIN g.aula a " +
            "WHERE (g.id = :grupoId OR a.id = :aulaId) AND h.dia = :diaSemana")
    List<Horario> buscarPorGrupoOAulaYDia(@Param("grupoId") Long grupoId,
                                          @Param("aulaId") Long aulaId,
                                          @Param("diaSemana") DiaSemana diaSemana);
}
