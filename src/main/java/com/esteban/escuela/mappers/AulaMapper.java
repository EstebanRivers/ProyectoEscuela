package com.esteban.escuela.mappers;

import com.esteban.escuela.dto.aulas.AulaRequest;
import com.esteban.escuela.dto.aulas.AulaResponse;
import com.esteban.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper implements CommonMapper<AulaRequest, AulaResponse, Aula>{
    @Override
    public Aula requestToEntity(AulaRequest request) {
        if (request == null) {return null;}

        return Aula.builder()
                .nombre(request.nombre())
                .capacidad(request.capacidad())
                .build();
    }

    @Override
    public AulaResponse entityToResponse(Aula entity) {
        if (entity == null) {return null;}

        return new  AulaResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getCapacidad());
    }
}
