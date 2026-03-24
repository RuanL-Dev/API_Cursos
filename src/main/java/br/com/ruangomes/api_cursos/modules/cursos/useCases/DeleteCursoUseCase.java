package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.ruangomes.api_cursos.exceptions.NoContentFoundException;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCursoUseCase {

    private final CursoRepository cursoRepository;

    public void deleteExecute(UUID id) {
        var curso = this.cursoRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NoContentFoundException();
                });
        this.cursoRepository.delete(curso);
    }
}
