package com.esteban.escuela.controllers;

import com.esteban.escuela.dto.grupos.GrupoRequest;
import com.esteban.escuela.dto.grupos.GrupoResponse;
import com.esteban.escuela.services.grupos.GrupoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController extends CommonController<GrupoRequest, GrupoResponse, GrupoService>{

    public GrupoController(GrupoService service) {
        super(service);
    }
}
