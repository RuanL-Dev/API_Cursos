package br.com.ruangomes.api_cursos.modules.cursos.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCursoResponseDTO {
    
    private String name;
    private String category;
    private String professor;
    private Boolean active;
    private UUID id;
}
