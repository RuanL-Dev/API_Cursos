package br.com.ruangomes.api_cursos.modules.professor;

import br.com.ruangomes.api_cursos.exceptions.ProfessorFoundException;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.modules.professor.useCases.CreateProfessorUseCase;

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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class CreateProfessorUseCaseTest {

    @InjectMocks
    private CreateProfessorUseCase createProfessorUseCase;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should not be able to create a professor with existing name")
    public void shouldNotBeAbleToCreateProfessorWithExistingName() {

        var professorEntityTest = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .nomeProfessor("qualquerNomeProfessor")
                .password("SenhaForte123!")
                .build();

        doReturn(Optional.of(professorEntityTest)).when(professorRepository).findByNomeProfessor(anyString());

        Assertions.assertThatThrownBy(() -> createProfessorUseCase.execute(professorEntityTest))
                .isInstanceOf(ProfessorFoundException.class);

        verify(passwordEncoder, never()).encode(anyString());
        verify(professorRepository, never()).save(professorEntityTest);
    }

    @Test
    @DisplayName("Should be able to create a professor")
    public void shouldBeAbleToCreateProfessor() {

        var professorEntityTest = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .nomeProfessor("qualquerNomeProfessor")
                .password("SenhaForte123!")
                .build();

        when(professorRepository.findByNomeProfessor(anyString())).thenReturn(Optional.empty());

        doReturn(professorEntityTest.getPassword()).when(passwordEncoder).encode(anyString());

        doReturn(professorEntityTest).when(professorRepository).save(any());

        Assertions.assertThat(createProfessorUseCase.execute(professorEntityTest)).isNotNull()
                .extracting(ProfessorEntity::getId, ProfessorEntity::getNomeProfessor, ProfessorEntity::getPassword)
                .containsExactly(professorEntityTest.getId(), professorEntityTest.getNomeProfessor(),
                        professorEntityTest.getPassword());

    }
}
