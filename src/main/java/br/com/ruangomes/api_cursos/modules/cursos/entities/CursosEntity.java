package br.com.ruangomes.api_cursos.modules.cursos.entities;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursosEntity {
    
    private UUID id;

    @NotBlank(message = "O campo [name] é obrigatório.")
    @Pattern(regexp = "^[^\\s]+$", message = "O campo [name] não deve conter espaços em branco.")
    private String name;
    private String category;
    private Boolean active;
    private Date created_at;
    private LocalDateTime updated_at;
}
