package br.com.ruangomes.api_cursos.modules.cursos.dto;

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

    private String name;
    private String category;
    @Pattern(regexp = "^[^\\s]+$", message = "O campo [nomeProfessor] não deve conter espaços em branco.")
    private String professor;

}
