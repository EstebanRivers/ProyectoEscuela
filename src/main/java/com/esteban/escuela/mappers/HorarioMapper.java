package com.esteban.escuela.mappers;

import com.esteban.escuela.dto.horarios.HorarioRequest;
import com.esteban.escuela.dto.horarios.HorarioResponse;
import com.esteban.escuela.entities.Horario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HorarioMapper implements CommonMapper<HorarioRequest, HorarioResponse, Horario>{
    private final GrupoMapper grupoMapper;

    @Override
    public Horario requestToEntity(HorarioRequest request) {
        if(request == null) return null;

        return Horario.builder()
                .dia(request.diaSemana())
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();

    }

    @Override
    public HorarioResponse entityToResponse(Horario entity) {
        if(entity == null) return null;

        return new HorarioResponse(
                entity.getId(),
                grupoMapper.grupoToGrupoResumen(entity.getGrupo()),
                horarioAcomodado(entity)

        );
    }

    public String horarioAcomodado(Horario horario) {
        if (horario == null) {return null;}

        return horario.getDia()+" "+horario.getHoraInicio()+" "+horario.getHoraFin();
    }

}
