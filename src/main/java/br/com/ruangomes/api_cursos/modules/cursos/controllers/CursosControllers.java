package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.CreateCursoUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.ProfileCursoUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/professor/")
public class CursosControllers {

    @Autowired
    private CreateCursoUseCase createCursoUseCase;

    @Autowired
    private ProfileCursoUseCase profileCursoUseCase;

    @PostMapping("/cursos")
    public ResponseEntity<Object> create( @Valid @RequestBody CursosEntity cursosEntity) {
        try {
            var result = this.createCursoUseCase.execute(cursosEntity);
            return ResponseEntity.ok().body(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
        
    }    

    @GetMapping("/")
    @PreAuthorize("hasRole('CURSO')")
    public ResponseEntity<Object> get(HttpServletRequest request) {
        
        var idCurso = request.getAttribute("professores_id");
        try {
            var profile = this.profileCursoUseCase.execute(UUID.fromString(idCurso.toString()));
            return ResponseEntity.ok().body(profile);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
}
