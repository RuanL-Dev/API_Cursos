package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import static org.mockito.Mockito.when;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;

@ExtendWith(MockitoExtension.class)
public class ListCursosUseCaseTest {

        @InjectMocks
        private ListCursosUseCase listCursosUseCase;

        @Mock
        private CursoRepository cursoRepository;

        @Test
        @DisplayName("Should be able to list courses with nameFilters")
        public void shouldBeAbleToListCoursesWithNameFilters() {

                String nameFilter = "qualquernome";
                String categoryFilter = null;

                var ProfessorEntityTest = ProfessorEntity.builder()
                                .id(UUID.randomUUID())
                                .nomeProfessor("qualquerNomeProfessor")
                                .password("SenhaForte123!")
                                .build();

                var CursosEntityTest = CursosEntity.builder()
                                .id(UUID.randomUUID())
                                .name("qualquernome")
                                .category("qualquerCategoria")
                                .professor(ProfessorEntityTest)
                                .build();

                when(cursoRepository.findByFilters("%qualquernome%", categoryFilter))
                                .thenReturn(List.of(CursosEntityTest));

                var response = listCursosUseCase.execute(nameFilter, categoryFilter);

                Assertions.assertThat(response).isNotNull();
                Assertions.assertThat(response).hasSize(1);
                Assertions.assertThat(response.get(0).getId()).isEqualTo(CursosEntityTest.getId());
                Assertions.assertThat(response.get(0).getName()).isEqualTo("qualquernome");
                Assertions.assertThat(response.get(0).getCategory()).isEqualTo("qualquerCategoria");
                Assertions.assertThat(response.get(0).getProfessor()).isEqualTo("qualquerNomeProfessor");

        }

        @Test
        @DisplayName("Should be able to list courses with categoryFilters")
        public void shouldBeAbleToListCoursesWithCategoryFilters() {
                String nameFilter = null;
                String categoryFilter = "backend";

                var ProfessorEntityTest = ProfessorEntity.builder()
                                .id(UUID.randomUUID())
                                .nomeProfessor("qualquerNomeProfessor")
                                .password("SenhaForte123!")
                                .build();

                var CursosEntityTest1 = CursosEntity.builder()
                                .id(UUID.randomUUID())
                                .name("qualquerNome1")
                                .category("backend")
                                .professor(ProfessorEntityTest)
                                .build();

                var CursosEntityTest2 = CursosEntity.builder()
                                .id(UUID.randomUUID())
                                .name("qualquerNome2")
                                .category("backend")
                                .professor(ProfessorEntityTest)
                                .build();

                when(cursoRepository.findByFilters(null, categoryFilter))
                                .thenReturn(List.of(CursosEntityTest1, CursosEntityTest2));

                var response = listCursosUseCase.execute(nameFilter, categoryFilter);

                Assertions.assertThat(response).isNotNull();
                Assertions.assertThat(response).hasSize(2);
                Assertions.assertThat(response.get(0).getId()).isEqualTo(CursosEntityTest1.getId());
                Assertions.assertThat(response.get(1).getId()).isEqualTo(CursosEntityTest2.getId());
                Assertions.assertThat(response.get(0).getName()).isEqualTo(CursosEntityTest1.getName());
                Assertions.assertThat(response.get(1).getName()).isEqualTo(CursosEntityTest2.getName());
                Assertions.assertThat(response.get(0).getCategory()).isEqualTo(CursosEntityTest1.getCategory());
                Assertions.assertThat(response.get(1).getCategory()).isEqualTo(CursosEntityTest2.getCategory());
                Assertions.assertThat(response.get(0).getProfessor()).isEqualTo("qualquerNomeProfessor");
                Assertions.assertThat(response.get(1).getProfessor()).isEqualTo("qualquerNomeProfessor");

        }

}
