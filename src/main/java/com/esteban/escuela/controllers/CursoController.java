package com.esteban.escuela.controllers;

import com.esteban.escuela.dto.cursos.CursoRequest;
import com.esteban.escuela.dto.cursos.CursoResponse;
import com.esteban.escuela.services.cursos.CursoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
public class CursoController extends  CommonController <CursoRequest, CursoResponse, CursoService>{
    public CursoController(CursoService service) {
        super(service);
    }
}
