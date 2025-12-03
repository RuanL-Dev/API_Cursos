package br.com.ruangomes.api_cursos.modules.professor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.useCases.CreateProfessorUseCase;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/professor/")
public class ProfessorControllers {
    
    @Autowired
    private CreateProfessorUseCase createProfessorUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody ProfessorEntity professorEntity) {
        
        try {
            var result = this.createProfessorUseCase.execute(professorEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
