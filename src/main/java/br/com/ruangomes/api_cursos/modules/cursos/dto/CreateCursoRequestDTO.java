package br.com.ruangomes.api_cursos.modules.cursos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCursoRequestDTO {

    @NotBlank(message = "O campo [name] é obrigatório.")
    @Schema(example = "Introdução ao Java", description = "Nome do curso.", requiredMode = RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "O campo [category] é obrigatório.")
    @Schema(example = "BackEnd", description = "Categoria do curso.", requiredMode = RequiredMode.REQUIRED)
    private String category;
    
}
