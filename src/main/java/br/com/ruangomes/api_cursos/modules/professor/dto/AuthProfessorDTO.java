package br.com.ruangomes.api_cursos.modules.professor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthProfessorDTO {
    private String nomeProfessor;
    private String password;
    
}
