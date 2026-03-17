package com.esteban.escuela.entities;

import com.esteban.escuela.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="HORARIOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Column(name = "DIA", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaSemana dia;

    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

}
