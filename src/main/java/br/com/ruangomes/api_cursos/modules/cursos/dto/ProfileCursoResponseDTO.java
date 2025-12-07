package br.com.ruangomes.api_cursos.modules.cursos.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCursoResponseDTO {

    @Schema(example = "Introdução ao Java", description = "Nome do curso.")
    private String name;

    @Schema(example = "Programação", description = "Categoria do curso.")
    private String category;

    @Schema(description = "Nome do professor responsável pelo curso. Preenchido automaticamente com base no token JWT.", 
    accessMode = Schema.AccessMode.READ_ONLY)
    private String professor;

    @Schema(description = "Indica se o curso está ativo.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active;

    @Schema(description = "ID único do curso.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
}
    