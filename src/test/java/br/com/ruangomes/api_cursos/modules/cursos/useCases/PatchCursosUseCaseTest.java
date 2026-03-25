package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.ruangomes.api_cursos.exceptions.NoContentException;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;

@ExtendWith(MockitoExtension.class)
public class PatchCursosUseCaseTest {

    @InjectMocks
    private PatchCursosUseCase patchCursosUseCase;

    @Mock
    private CursoRepository cursoRepository;

    @Test
    @DisplayName("Should not be able to patch a course's with non existing id")
    public void shouldNotBeAbleToPatchCourseWithNonExistingId() {
        var nonExistingCourseId = UUID.randomUUID();

        when(cursoRepository.findById(nonExistingCourseId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> patchCursosUseCase.patchExecute(nonExistingCourseId))
                .isInstanceOf(NoContentException.class);
    }

    @Test
    @DisplayName("Should be able to patch a course's active status by id")
    public void shouldBeAbleToPatchCourseActiveStatusById() {
        var courseId = UUID.randomUUID();

        var professorEntityTest = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .nomeProfessor("qualquerNomeProfessor")
                .password("SenhaForte123!")
                .build();

        var cursoEntityTest = CursosEntity.builder()
                .id(courseId)
                .name("qualquerNome")
                .category("qualquerCategoria")
                .professor(professorEntityTest)
                .build();

        when(cursoRepository.findById(courseId)).thenReturn(Optional.of(cursoEntityTest));

        when(cursoRepository.save(cursoEntityTest)).thenReturn(cursoEntityTest);

        var response = patchCursosUseCase.patchExecute(courseId);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getActive()).isFalse();

    }
}
