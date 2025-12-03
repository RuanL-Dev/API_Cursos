package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;

@Service
public class ProfileCursoUseCase {
    
    @Autowired
    private CursoRepository cursoRepository;

    public ProfileCursoResponseDTO execute (UUID idCurso) {
        var curso = this.cursoRepository.findById(idCurso)
        .orElseThrow(() -> {
            throw new UsernameNotFoundException("Curso not found");

        });
        var cursoDTO = ProfileCursoResponseDTO.builder()
        .name(curso.getName())
        .category(curso.getCategory())
        .professor(curso.getProfessor().getNomeProfessor())
        .active(curso.getActive())
        .id(curso.getId())
        .build();

        return cursoDTO;

    }
}
