package br.com.ruangomes.api_cursos.modules.cursos.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;

public interface CursoRepository extends JpaRepository<CursosEntity, UUID> {
    Optional<CursosEntity> findByNameOrCategory(String name, String category);
    Optional<CursosEntity> findByName(String name);
}
