package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.ruangomes.api_cursos.exceptions.CursoFoundException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.CreateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateCursoUseCase {
    

    private final CursoRepository cursoRepository;

    private final ProfessorRepository professorRepository;


    public ProfileCursoResponseDTO execute(CreateCursoRequestDTO createCursoRequestDTO) {
        this.cursoRepository.findByNameIgnoreCase(createCursoRequestDTO.getName())
        .ifPresent((user) -> {
            throw new CursoFoundException();
        });

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var subject = authentication.getName();

        UUID professorId;
        try {
            professorId = UUID.fromString(subject);
        } catch (IllegalArgumentException  e) {
            throw new RuntimeException("Token inválido: subject não é um UUID");
        }
        

        ProfessorEntity professor = professorRepository.findById(professorId)
        .orElseThrow(() -> {
            throw new RuntimeException("Professor not found");
        });

        var cursoBuilder = CursosEntity.builder()
        .name(createCursoRequestDTO.getName())
        .category(createCursoRequestDTO.getCategory())
        .professor(professor)
        .build();

        var saved = this.cursoRepository.save(cursoBuilder);

        var cursoDTO = ProfileCursoResponseDTO.builder()
        .name(saved.getName())
        .category(saved.getCategory())
        .professor(saved.getProfessor().getNomeProfessor())
        .active(saved.getActive())
        .id(saved.getId())
        .build();

        return cursoDTO;
    }
}
