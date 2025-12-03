package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.ruangomes.api_cursos.exceptions.NoContentException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;

@Service
public class ListCursosUseCase {

    @Autowired
    private CursoRepository cursoRepository;

    public List<ProfileCursoResponseDTO> execute() {
        var result = this.cursoRepository.findAll();
        if (result.isEmpty()) {
            throw new NoContentException();
        }

        var cursoDTO = result.stream().map(curso -> ProfileCursoResponseDTO.builder()
                .name(curso.getName())
                .category(curso.getCategory())
                .professor(curso.getProfessor().getNomeProfessor())
                .active(curso.getActive())
                .id(curso.getId())
                .build()).toList();

        return cursoDTO;
    }
}
