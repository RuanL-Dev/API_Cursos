package br.com.ruangomes.api_cursos.modules.cursos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCursoRequestDTO {

    @Schema(example = "Introdução ao Java", description = "Nome do curso.")
    private String name;

    @Schema(example = "Programação", description = "Categoria do curso.")
    private String category;

    @Pattern(regexp = "^[^\\s]+$", message = "O campo [nomeProfessor] não deve conter espaços em branco.")
    @Schema(example = "joaosilva", description = "Nome do professor, sem espaços.")
    private String professor;

}
