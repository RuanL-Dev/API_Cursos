package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.ruangomes.api_cursos.exceptions.CursoFoundException;
import br.com.ruangomes.api_cursos.modules.cursos.dto.CreateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
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

                verify(cursoRepository, never()).save(any(CursosEntity.class)); // Verifica que o método save não foi
                                                                                // chamado

        }

        @Test
        @DisplayName("Should not be able to create a course with an invalid professor ID in the token")
        public void shouldNotBeAbleToCreateCourseWithInvalidProfessorIdInToken() {

                var request = CreateCursoRequestDTO.builder()
                                .name("qualquer_nome")
                                .category("qualquer_categoria")
                                .build();

                when(cursoRepository.findByNameIgnoreCase(anyString()))
                                .thenReturn(Optional.empty());

                String invalidTokenSubject = "invalid-uuid";

                var auth = new UsernamePasswordAuthenticationToken(invalidTokenSubject, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

                Assertions.assertThatThrownBy(() -> createCursoUseCase.execute(request))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Token inválido: subject não é um UUID");

                verify(professorRepository, never()).findById(any(UUID.class));

                verify(cursoRepository, never()).save(any(CursosEntity.class));

        }

        @Test
        @DisplayName("Should not be able to create a course with professor not found in the database")
        public void shouldNotBeAbleToCreateCourseWithProfessorNotFoundInDatabase() {

                var request = CreateCursoRequestDTO.builder()
                                .name("qualquer_nome")
                                .category("qualquer_categoria")
                                .build();

                when(cursoRepository.findByNameIgnoreCase(anyString()))
                                .thenReturn(Optional.empty());

                String validTokenSubjectTest = "123e4567-e89b-12d3-a456-426614174000";
                var auth = new UsernamePasswordAuthenticationToken(validTokenSubjectTest, null,
                                Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

                when(professorRepository.findById(any(UUID.class)))
                                .thenReturn(Optional.empty());

                Assertions.assertThatThrownBy(() -> createCursoUseCase.execute(request))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Professor not found");

                verify(cursoRepository, never()).save(any(CursosEntity.class));

        }

        @Test
        @DisplayName("Should be able to create a course successfully")
        public void shouldBeAbleToCreateCourseSuccessfully() {

                var request = CreateCursoRequestDTO.builder()
                                .name("Introdução ao Java")
                                .category("backend")
                                .build();

                when(cursoRepository.findByNameIgnoreCase(anyString()))
                                .thenReturn(Optional.empty());

                String validTokenSubjectTest = "123e4567-e89b-12d3-a456-426614174000";
                var auth = new UsernamePasswordAuthenticationToken(validTokenSubjectTest, null,
                                Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

                var professorEntityTest = ProfessorEntity.builder()
                                .id(UUID.fromString(validTokenSubjectTest))
                                .nomeProfessor("qualquer_nome")
                                .password("SenhaForte123!")
                                .build();

                when(professorRepository.findById(any(UUID.class)))
                                .thenReturn(Optional.of(professorEntityTest));

                var savedCursoEntityTest = CursosEntity.builder()
                                .id(UUID.randomUUID())
                                .name(request.getName())
                                .category(request.getCategory())
                                .professor(professorEntityTest)
                                .build();

                when(cursoRepository.save(any(CursosEntity.class)))
                                .thenReturn(savedCursoEntityTest);

                var response = createCursoUseCase.execute(request);

                Assertions.assertThat(response).isNotNull();
                Assertions.assertThat(response.getId()).isEqualTo(savedCursoEntityTest.getId());
                Assertions.assertThat(response.getName()).isEqualTo(savedCursoEntityTest.getName());
                Assertions.assertThat(response.getCategory()).isEqualTo(savedCursoEntityTest.getCategory());
                Assertions.assertThat(response.getProfessor())
                                .isEqualTo(savedCursoEntityTest.getProfessor().getNomeProfessor());
                Assertions.assertThat(response.getActive()).isEqualTo(savedCursoEntityTest.getActive());

        }

        @AfterEach
        void clearSecurityContext() {
                SecurityContextHolder.clearContext(); // Limpa o contexto de segurança após cada teste para evitar
                                                      // interferências entre os testes
        }

}
