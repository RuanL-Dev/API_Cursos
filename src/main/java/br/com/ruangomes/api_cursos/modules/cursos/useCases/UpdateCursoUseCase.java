package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import java.util.UUID;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.ruangomes.api_cursos.exceptions.NoContentException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.UpdateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCursoUseCase {

    private final CursoRepository cursoRepository;
    private final ProfessorRepository professorRepository;

    public UpdateCursoRequestDTO updateExecute(UUID id, UpdateCursoRequestDTO data) {

        var curso = this.cursoRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NoContentException();
                });

        boolean dataUpdated = false;

        if (data.getName() != null && !data.getName().isBlank()) {
            curso.setName(data.getName());
            dataUpdated = true;
        }

        if (data.getCategory() != null && !data.getCategory().isBlank()) {
            curso.setCategory(data.getCategory());
            dataUpdated = true;
        }

        if (data.getProfessor() != null && !data.getProfessor().isBlank()) {
            var professor = this.professorRepository.findByNomeProfessor(data.getProfessor())
                    .orElseThrow(() -> {
                        throw new UsernameNotFoundException("Professor não encontrado.");
                    });
            curso.setProfessor(professor);
            dataUpdated = true;
        }

        if (!dataUpdated) {
            throw new IllegalArgumentException("Nenhum dado fornecido para atualização.");
        }

        var updateCurso = this.cursoRepository.save(curso);

        var cursoUpdatedDTO = UpdateCursoRequestDTO.builder()
                .name(updateCurso.getName())
                .category(updateCurso.getCategory())
                .professor(updateCurso.getProfessor().getNomeProfessor())
                .build();

        return cursoUpdatedDTO;
    }
}
