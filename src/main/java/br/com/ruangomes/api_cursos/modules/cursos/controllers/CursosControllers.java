package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruangomes.api_cursos.modules.cursos.dto.CreateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.dto.UpdateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.CreateCursoUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.DeleteCursoUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.ListCursosUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.PatchCursosUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.UpdateCursoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/professor/")
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "Endpoints para gerenciamento de cursos")
public class CursosControllers {

    private final CreateCursoUseCase createCursoUseCase;

    private final ListCursosUseCase listCursosUseCase;

    private final UpdateCursoUseCase updateCursoUseCase;

    private final DeleteCursoUseCase deleteCursoUseCase;

    private final PatchCursosUseCase patchCursosUseCase;

    @PostMapping("/cursos")
    @PreAuthorize("hasRole('PROFESSOR')")
    @Operation(summary = "Criar um novo curso", description = "Endpoint para criação de um novo curso. Requer autenticação de professor.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CreateCursoRequestDTO.class))
            }, description = "Curso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nome de curso já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Professor not found"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Token inválido ou ausente")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CreateCursoRequestDTO createCursoRequestDTO) {
        try {
            var result = this.createCursoUseCase.execute(createCursoRequestDTO);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/cursos")
    @PreAuthorize("hasRole('PROFESSOR')")
    @Operation(summary = "Listar cursos", description = "Endpoint para listar cursos com filtros opcionais. Requer autenticação de professor.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ProfileCursoResponseDTO.class)))
            }, description = "Lista de cursos retornada com sucesso (pode ser vazia se não houver cursos cadastrados)"),
            @ApiResponse(responseCode = "204", description = "Nenhum curso cadastrado", content = @Content),
    })
    public ResponseEntity<List<ProfileCursoResponseDTO>> listarCursos(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        var result = this.listCursosUseCase.execute(name, category);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);

    }

    @PutMapping("/cursos/{id}")
    @Operation(summary = "Atualizar curso", description = "Endpoint para atualizar um curso existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UpdateCursoRequestDTO.class))
            }, description = "Curso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "No content available/registered"),
            @ApiResponse(responseCode = "400", description = "Professor não encontrado"),
            @ApiResponse(responseCode = "400", description = "Nenhum dado fornecido para atualização")
    })
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

    @DeleteMapping("/cursos/{id}")
    @Operation(summary = "Deletar curso", description = "Endpoint para deletar um curso existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Curso não encontrado.")
    })
    public ResponseEntity<Object> deleteCurso(@PathVariable UUID id) {
        this.deleteCursoUseCase.deleteExecute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cursos/{id}/active")
    @Operation(summary = "Ativar/Desativar curso", description = "Endpoint para ativar ou desativar um curso existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Curso atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "No content available/registered")
    })
    public ResponseEntity<Object> patchCurso(@PathVariable UUID id) {
        try {
            this.patchCursosUseCase.patchExecute(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
