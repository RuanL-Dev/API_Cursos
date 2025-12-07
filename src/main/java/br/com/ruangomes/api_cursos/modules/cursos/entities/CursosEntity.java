package br.com.ruangomes.api_cursos.modules.cursos.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cursos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursosEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo [name] é obrigatório.")
    @Schema(example = "Introdução ao Java", description = "Nome do curso.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "O campo [category] é obrigatório.")
    @Schema(example = "BackEnd", description = "Categoria do curso.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @ManyToOne
    @JoinColumn(name = "professores_id", nullable = false)
    @Schema(description = "Professor responsável pelo curso.")
    private ProfessorEntity professor;

    @Builder.Default
    @Schema(description = "Indica se o curso está ativo.", example = "true")
    private Boolean active = true;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }

    @CreationTimestamp
    @Schema(description = "Data de criação do curso.", example = "2023-01-01T12:00:00")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Schema(description = "Data da última atualização do curso.", example = "2023-01-02T12:00:00")
    private LocalDateTime updated_at;
}
