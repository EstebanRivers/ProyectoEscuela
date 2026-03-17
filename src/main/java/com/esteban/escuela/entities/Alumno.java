package com.esteban.escuela.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="ALUMNOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apellidoMaterno;

    @Column(name = "EMAIL", length = 100, nullable = true, unique = true)
    private String email;

    @Column(name = "MATRICULA", length = 10, nullable = true,  unique = true)
    private String matricula;

    @Column(name = "FECHA_INGRESO")
    private LocalDate fechaIngreso;
}
