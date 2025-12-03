package br.com.ruangomes.api_cursos.modules.cursos.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;

public interface CursoRepository extends JpaRepository<CursosEntity, UUID> {
    @Query("""
            Select c FROM cursos c
            WHERE (:name IS NULL OR LOWER(c.name) LIKE :name)
            AND (:category IS NULL OR LOWER(c.category) = :category)
            """)
        List<CursosEntity> findByFilters(
            @Param("name") String name, 
            @Param("category") String category);

    Optional<CursosEntity> findByNameIgnoreCase(String name);

    List<CursosEntity> findByProfessor_NomeProfessor(String nomeProfessor);

    List<CursosEntity> findByProfessor_Id(UUID professorId);
}
