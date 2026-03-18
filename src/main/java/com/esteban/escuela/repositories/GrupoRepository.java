package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    boolean existsByMaestroId(Long maestroId);

    boolean existsByAulaId(Long aulaId);
}
