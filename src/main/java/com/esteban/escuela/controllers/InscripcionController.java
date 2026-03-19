package com.esteban.escuela.controllers;

import com.esteban.escuela.dto.inscripciones.InscripcionRequest;
import com.esteban.escuela.dto.inscripciones.InscripcionResponse;
import com.esteban.escuela.services.inscripciones.InscripcionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController extends CommonController<InscripcionRequest, InscripcionResponse, InscripcionService>{
    public InscripcionController(InscripcionService service) {
        super(service);
    }
}
