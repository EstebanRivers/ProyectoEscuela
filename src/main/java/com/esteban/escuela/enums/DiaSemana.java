package com.esteban.escuela.enums;

import com.esteban.escuela.exceptions.RecursoNoEncontrado;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado");

    private final String descripcion;

    private static String quitarAcentos(String s) {
        return s.toLowerCase()
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u").replace("ü", "u");
    }

    public static DiaSemana fromDescripcion(String descripcion) {
        String buscado = quitarAcentos(descripcion.trim());
        for (DiaSemana dia : values()) {
            String descDia = quitarAcentos(dia.descripcion);
            if (descDia.equalsIgnoreCase(buscado)) {
                return dia;
            }
        }
        throw new RecursoNoEncontrado("No existe dia de la semana con la description: " + descripcion);
    }

}
