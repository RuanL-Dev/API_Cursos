package br.com.ruangomes.api_cursos.modules.cursos.entities;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

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

@Entity(name = "cursos")//annotation para definir o nome da tabela no banco de dados, feita pelo spring data jpa
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursosEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo [name] é obrigatório.")
    @Pattern(regexp = "^[^\\s]+$", message = "O campo [name] não deve conter espaços em branco.")
    private String name;

    @NotBlank(message = "O campo [password] é obrigatório.")
    @Length(min = 10, max = 100, message = "O campo [password] deve ter entre (10) e (100) caracteres.")
    private String password;

    @NotBlank(message = "O campo [category] é obrigatório.")
    private String category;

    
    private Boolean active;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
}
