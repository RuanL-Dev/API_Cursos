package br.com.ruangomes.api_cursos.modules.cursos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCursoDTO {
    private String name;
    private String password;
    
}
