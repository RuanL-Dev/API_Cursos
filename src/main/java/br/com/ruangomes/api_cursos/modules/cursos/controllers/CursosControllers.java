package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cursos")
public class CursosControllers {

    @PostMapping("/")
    public void create( @Valid @RequestBody CursosEntity cursosEntity) {
        System.out.println("Cursos");
        System.out.println(cursosEntity.getName());
        
        
    }
    
    
}
