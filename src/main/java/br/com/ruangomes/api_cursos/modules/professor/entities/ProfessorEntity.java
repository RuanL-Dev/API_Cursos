package br.com.ruangomes.api_cursos.modules.professor.entities;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "professores")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo [nomeProfessor] é obrigatório.")
    @Pattern(regexp = "^[^\\s]+$", message = "O campo [nomeProfessor] não deve conter espaços em branco.")
    @Schema(example = "joaosilva", description = "Nome do professor, sem espaços.", requiredMode = RequiredMode.REQUIRED)
    private String nomeProfessor;

    @NotBlank(message = "O campo [password] é obrigatório.")
    @Length(min = 10, max = 100, message = "O campo [password] deve ter entre (10) e (100) caracteres.")
    @Schema(example = "SenhaForte123!", minLength = 10, maxLength = 100, description = "Senha do professor - deve conter entre (10) e (100) caracteres.", requiredMode = RequiredMode.REQUIRED)
    private String password;
}
