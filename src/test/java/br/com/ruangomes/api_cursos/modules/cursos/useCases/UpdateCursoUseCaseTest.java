package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import br.com.ruangomes.api_cursos.exceptions.NoContentException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.UpdateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateCursoUseCaseTest {

        @InjectMocks
        private UpdateCursoUseCase updateCursoUseCase;

        @Mock
        private CursoRepository cursoRepository;

        @Mock
        private ProfessorRepository professorRepository;

        @Test
        @DisplayName("Should not be able to update a course with non existing id")
        public void shouldNotBeAbleToUpdateCourseWithNonExistingId() {

                var nonExistingCourseId = UUID.randomUUID();

                when(cursoRepository.findById(nonExistingCourseId)).thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> updateCursoUseCase.updateExecute(nonExistingCourseId, null))
                                .isInstanceOf(NoContentException.class);
        }

        @Test
        @DisplayName("Should not be able to update a course with non existing professor")
        public void shouldNotBeAbleToUpdateCourseWithNonExistingProfessor() {

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

                var updateCursoRequestTestDTO = UpdateCursoRequestDTO.builder()
                                .name("qualquerNome")
                                .category("qualquerCategoria")
                                .professor("qualquerProfessor")
                                .build();

                when(cursoRepository.findById(courseId)).thenReturn(Optional.of(cursoEntityTest));

                when(professorRepository.findByNomeProfessor(updateCursoRequestTestDTO.getProfessor()))
                                .thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(
                                () -> updateCursoUseCase.updateExecute(courseId, updateCursoRequestTestDTO))
                                .isInstanceOf(UsernameNotFoundException.class);
        }

        @Test
        @DisplayName("Should be able to update a course by id")
        public void shouldBeAbleToUpdateCourseById() {

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

                var updateCursoRequestTestDTO = UpdateCursoRequestDTO.builder()
                                .name("")
                                .category("")
                                .professor("")
                                .build();

                when(cursoRepository.findById(courseId)).thenReturn(Optional.of(cursoEntityTest));

                Assertions.assertThatThrownBy(
                                () -> updateCursoUseCase.updateExecute(courseId, updateCursoRequestTestDTO))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Nenhum dado fornecido para atualização.");

                verify(professorRepository, never()).findByNomeProfessor(anyString());
                verify(cursoRepository, never()).save(any(CursosEntity.class));
        }

        @Test
        @DisplayName("Should be able to update a course successfully")
        public void shouldBeAbleToUpdateCourseSuccessfully() {

                var courseId = UUID.randomUUID();

                var professorEntityTest = ProfessorEntity.builder()
                                .id(UUID.randomUUID())
                                .nomeProfessor("qualquerNomeProfessor")
                                .password("SenhaForte123!")
                                .build();

                var professorEntityUpdatedTest = ProfessorEntity.builder()
                                .id(UUID.randomUUID())
                                .nomeProfessor("qualquerNomeProfessorAtualizado")
                                .password("SenhaForte123!")
                                .build();

                var cursoEntityTest = CursosEntity.builder()
                                .id(courseId)
                                .name("qualquerNome")
                                .category("qualquerCategoria")
                                .professor(professorEntityTest)
                                .build();

                var updateCursoRequestTestDTO = UpdateCursoRequestDTO.builder()
                                .name("qualquerNomeAtualizado")
                                .category("qualquerCategoriaAtualizada")
                                .professor("qualquerNomeProfessorAtualizado")
                                .build();

                when(cursoRepository.findById(courseId)).thenReturn(Optional.of(cursoEntityTest));

                String professorNameToSearch = updateCursoRequestTestDTO.getProfessor();

                doReturn(Optional.of(professorEntityUpdatedTest))
                                .when(professorRepository).findByNomeProfessor(professorNameToSearch);

                when(cursoRepository.save(any(CursosEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
                // Simula o comportamento do método save para retornar a entidade salva

                var response = updateCursoUseCase.updateExecute(courseId, updateCursoRequestTestDTO);

                Assertions.assertThat(response).isNotNull()
                                .extracting("name", "category", "professor")
                                .containsExactly(updateCursoRequestTestDTO.getName(),
                                                updateCursoRequestTestDTO.getCategory(),
                                                updateCursoRequestTestDTO.getProfessor());

        }

}
