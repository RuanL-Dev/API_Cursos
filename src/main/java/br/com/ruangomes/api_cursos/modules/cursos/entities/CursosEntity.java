package br.com.ruangomes.api_cursos.modules.cursos.entities;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private String name;
    private String category;
    private Boolean active;
    private Date created_at;
    private LocalDateTime updated_at;
}
