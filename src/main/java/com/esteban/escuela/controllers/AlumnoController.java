package com.esteban.escuela.controllers;

import com.esteban.escuela.dto.alumnos.AlumnoRequest;
import com.esteban.escuela.dto.alumnos.AlumnoResponse;
import com.esteban.escuela.services.alumnos.AlumnoServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController extends CommonController<AlumnoRequest, AlumnoResponse, AlumnoServices>{

    public AlumnoController(AlumnoServices service) {
        super(service);
    }
}
