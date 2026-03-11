package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ruangomes.api_cursos.exceptions.CursoFoundException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.CreateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;

@ExtendWith(MockitoExtension.class)
public class CreateCursoUseCaseTest {

    @InjectMocks
    private CreateCursoUseCase createCursoUseCase;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Test
    @DisplayName("Should not be able to create a course with an existing name")
    public void shouldNotBeAbleToCreateCourseWithExistingName() {

        var request = CreateCursoRequestDTO.builder()
                .name("Introdução ao Java")
                .category("backend")
                .build();

        var existingCourse = CursosEntity.builder()
                .id(UUID.randomUUID())
                .name("Introdução ao Java")
                .category("backend")
                .build();

        when(cursoRepository.findByNameIgnoreCase("Introdução ao Java"))
                .thenReturn(Optional.of(existingCourse));

        Assertions.assertThatThrownBy(() -> createCursoUseCase.execute(request))
                .isInstanceOf(CursoFoundException.class);

        verify(professorRepository, never()).findById(any(UUID.class)); // Verifica que o método findById do
                                                                        // professorRepository não foi chamado

        verify(cursoRepository, never()).save(any(CursosEntity.class)); // Verifica que o método save não foi chamado

    }

}
