package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import java.util.UUID;
import org.springframework.stereotype.Service;
import br.com.ruangomes.api_cursos.exceptions.NoContentException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO.ProfileCursoResponseDTOBuilder;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatchCursosUseCase {

    private final CursoRepository cursoRepository;

    public ProfileCursoResponseDTOBuilder patchExecute(UUID id) {

        var curso = this.cursoRepository.findById(id)
        .orElseThrow(() -> {
            throw new NoContentException();
        });

        Boolean currentStatus = Boolean.TRUE.equals(curso.getActive());
        curso.setActive(!currentStatus);

        var pacthedCurso = this.cursoRepository.save(curso);

        return ProfileCursoResponseDTO.builder()
        .name(pacthedCurso.getName())
        .category(pacthedCurso.getCategory())
        .professor(pacthedCurso.getProfessor().getNomeProfessor())
        .active(pacthedCurso.getActive())
        .id(pacthedCurso.getId());
    }
    
}
