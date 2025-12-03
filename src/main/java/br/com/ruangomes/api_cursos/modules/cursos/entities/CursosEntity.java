package br.com.ruangomes.api_cursos.modules.cursos.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
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
    private String name;

    @NotBlank(message = "O campo [category] é obrigatório.")
    private String category;

    @ManyToOne
    @JoinColumn(name = "professores_id", nullable = false)
    private ProfessorEntity professor;

    @Builder.Default
    private Boolean active = true;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
}
