package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.CreateCursoUseCase;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cursos")
public class CursosControllers {

    @Autowired
    private CreateCursoUseCase createCursoUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> create( @Valid @RequestBody CursosEntity cursosEntity) {
        try {
            var result = this.createCursoUseCase.execute(cursosEntity);
            return ResponseEntity.ok().body(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
        
    }
    
    
}
