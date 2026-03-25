package br.com.ruangomes.api_cursos.modules.cursos.useCases;

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

import br.com.ruangomes.api_cursos.exceptions.NoContentException;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;

@ExtendWith(MockitoExtension.class)
public class DeleteCursoUseCaseTest {

    @InjectMocks
    private DeleteCursoUseCase deleteCursoUseCase;

    @Mock
    private CursoRepository cursoRepository;

    @Test
    @DisplayName("Should not be able to delete a course with non existing id")
    public void shouldNotBeAbleToDeleteCourseWithNonExistingId() {
        var nonExistingId = UUID.randomUUID();
        when(cursoRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> deleteCursoUseCase.deleteExecute(nonExistingId))
                .isInstanceOf(NoContentException.class)
                .hasMessage("No content available/registered");

    }

    @Test
    @DisplayName("Should be able to delete a course by id")
    public void shouldBeAbleToDeleteCourseById() {
        var cursoId = UUID.randomUUID();
        var cursoEntityTest = CursosEntity.builder()
                .id(cursoId)
                .name("qualquer_nome")
                .category("qualquer_categoria")
                .build();
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(cursoEntityTest));

        Assertions.assertThatCode(() -> deleteCursoUseCase.deleteExecute(cursoId)).doesNotThrowAnyException();

    }

}
