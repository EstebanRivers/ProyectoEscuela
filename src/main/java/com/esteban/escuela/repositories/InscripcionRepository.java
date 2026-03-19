package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    boolean existsByAlumnoId(Long alumnoId);

    boolean existsByGrupoIdAndAlumnoId(Long grupoId, Long alumnoId);
    boolean existsByGrupoIdAndAlumnoIdAndIdNot(Long grupoId, Long alumnoId,  Long id);
}
