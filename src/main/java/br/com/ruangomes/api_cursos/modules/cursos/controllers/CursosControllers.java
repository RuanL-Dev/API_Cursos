package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cursos")
public class CursosControllers {

    @PostMapping("/")
    public void create(@RequestBody CursosEntity cursosEntity) {
        
        
        
    }
    
    
}
