package com.esteban.escuela.dto.datos;

import java.time.LocalDate;

public record DatosAlumno(
        String nombre,
        String matricula,
        String email,
        String fechaIngreso

) {
}
