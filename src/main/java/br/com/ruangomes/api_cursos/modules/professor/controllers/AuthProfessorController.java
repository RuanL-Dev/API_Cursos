package br.com.ruangomes.api_cursos.modules.professor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.professor.dto.AuthProfessorDTO;
import br.com.ruangomes.api_cursos.modules.professor.useCases.AuthProfessorUseCase;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/professor")
public class AuthProfessorController {
    
    @Autowired
    private AuthProfessorUseCase authProfessorUseCase;

    @PostMapping("/auth")
    public ResponseEntity<Object> create(@RequestBody AuthProfessorDTO authProfessorDTO) {
        try {
            var result = this.authProfessorUseCase.execute(authProfessorDTO);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
