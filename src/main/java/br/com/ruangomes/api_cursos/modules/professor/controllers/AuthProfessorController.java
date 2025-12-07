package br.com.ruangomes.api_cursos.modules.professor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.ruangomes.api_cursos.modules.professor.dto.AuthProfessorDTO;
import br.com.ruangomes.api_cursos.modules.professor.useCases.AuthProfessorUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/professor")
@Tag(name = "Professor", description = "Endpoints para gerenciamento de professores")
public class AuthProfessorController {

    @Autowired
    private AuthProfessorUseCase authProfessorUseCase;

    @PostMapping("/auth")
    @Operation(summary = "Autenticar professor", description = "Endpoint para autenticação de professores via token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AuthProfessorDTO.class))
            }, description = "Autenticação bem-sucedida"),
            @ApiResponse(responseCode = "401", description = "Professor/password incorrect")
    })
    public ResponseEntity<Object> create(@RequestBody AuthProfessorDTO authProfessorDTO) {
        try {
            var result = this.authProfessorUseCase.execute(authProfessorDTO);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
