package br.com.ruangomes.api_cursos.modules.professor.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyProfessorDTO {

    @Id
    private UUID id;

    @NotBlank(message = "O campo [nomeProfessor] é obrigatório.")
    @Pattern(regexp = "^[^\\s]+$", message = "O campo [nomeProfessor] não deve conter espaços em branco.")
    @Schema(example = "joaosilva", description = "Nome do professor, sem espaços.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeProfessor;
}
