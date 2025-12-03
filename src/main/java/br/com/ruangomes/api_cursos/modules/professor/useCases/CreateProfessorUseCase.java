package br.com.ruangomes.api_cursos.modules.professor.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ruangomes.api_cursos.exceptions.ProfessorFoundException;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;

@Service
public class CreateProfessorUseCase {
    
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ProfessorEntity execute(ProfessorEntity professorEntity) {
        this.professorRepository.findByNomeProfessor(professorEntity.getNomeProfessor())
        .ifPresent((user) -> {
            throw new ProfessorFoundException();
        });

        var password = passwordEncoder.encode(professorEntity.getPassword());
        professorEntity.setPassword(password);
    
        return this.professorRepository.save(professorEntity);
    }
}
