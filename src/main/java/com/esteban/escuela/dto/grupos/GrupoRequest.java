package com.esteban.escuela.dto.grupos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GrupoRequest(

        @NotNull(message = "La id del curso es requerida")
        Long idCurso,

        @NotNull(message = "La id del maestro es requerida")
        Long idMaestro,

        @NotNull(message = "La id del aula es requerida")
        Long idAula,

        @NotBlank(message = "El periodo es requerido")
        @Size(max = 20)
        String periodo
) {
}
