package com.esteban.escuela.controllers;

import com.esteban.escuela.dto.calificaiones.CalificacionRequest;
import com.esteban.escuela.dto.calificaiones.CalificacionResponse;
import com.esteban.escuela.services.calificaciones.CalificacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController extends CommonController<CalificacionRequest, CalificacionResponse, CalificacionService>{
    public CalificacionController(CalificacionService service) {
        super(service);
    }
}
