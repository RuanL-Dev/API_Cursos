package br.com.ruangomes.api_cursos.modules.professor.entities;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
    private String nomeProfessor;

    @NotBlank(message = "O campo [password] é obrigatório.")
    @Length(min = 10, max = 100, message = "O campo [password] deve ter entre (10) e (100) caracteres.")
    private String password;
}
