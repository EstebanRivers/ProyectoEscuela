package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    boolean existsByInscripcionId(Long inscripcionId);
    boolean existsByInscripcionIdAndIdNot(Long inscripcionId, Long id);

}
