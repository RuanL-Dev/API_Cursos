package br.com.ruangomes.api_cursos.modules.professor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.useCases.CreateProfessorUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/professor/")
@Tag(name = "Professor", description = "Endpoints para gerenciamento de professores")
public class ProfessorControllers {

    @Autowired
    private CreateProfessorUseCase createProfessorUseCase;

    @PostMapping("/")
    @Operation(summary = "Criar um novo professor", description = "Endpoint para criação de um novo professor.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProfessorEntity.class))
            }, description = "Professor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nome de professor já cadastrado")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody ProfessorEntity professorEntity) {

        try {
            var result = this.createProfessorUseCase.execute(professorEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
