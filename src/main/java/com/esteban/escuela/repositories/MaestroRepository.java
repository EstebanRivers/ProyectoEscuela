package com.esteban.escuela.repositories;

import com.esteban.escuela.entities.Maestro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaestroRepository extends JpaRepository<Maestro, Long> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByTelefono(String telefono);

    boolean existsByTelefonoAndIdNot(String telefono, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
