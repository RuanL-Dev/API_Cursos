package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.dto.UpdateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.CreateCursoUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.ListCursosUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.UpdateCursoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/professor/")
public class CursosControllers {

    @Autowired
    private CreateCursoUseCase createCursoUseCase;

    @Autowired
    private ListCursosUseCase listCursosUseCase;

    @Autowired
    private UpdateCursoUseCase updateCursoUseCase;

    @PostMapping("/cursos")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Object> create(@Valid @RequestBody CursosEntity cursosEntity) {
        try {
            var result = this.createCursoUseCase.execute(cursosEntity);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/cursos")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<ProfileCursoResponseDTO>> listarCursos(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        var result = this.listCursosUseCase.execute(name, category);
        return ResponseEntity.ok().body(result);

    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<Object> updateCurso(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCursoRequestDTO body) {
        try {
            var result = this.updateCursoUseCase.updateExecute(id, body);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
