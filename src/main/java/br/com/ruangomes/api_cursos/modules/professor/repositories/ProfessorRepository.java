package br.com.ruangomes.api_cursos.modules.professor.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;


public interface ProfessorRepository extends JpaRepository<ProfessorEntity, UUID> {
    Optional<ProfessorEntity> findByNomeProfessor(String nomeProfessor);
   
}
