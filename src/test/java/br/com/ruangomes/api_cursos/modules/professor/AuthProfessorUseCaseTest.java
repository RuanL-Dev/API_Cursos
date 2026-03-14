package br.com.ruangomes.api_cursos.modules.professor;

import br.com.ruangomes.api_cursos.modules.professor.dto.AuthProfessorDTO;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.modules.professor.useCases.AuthProfessorUseCase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@ExtendWith(MockitoExtension.class)
public class AuthProfessorUseCaseTest {

    @InjectMocks
    private AuthProfessorUseCase authProfessorUseCase;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthProfessorDTO authProfessorDTO;

    @Test
    @DisplayName("Should not be able to authenticate with non existing professor")
    public void shouldNotBeAbleToAuthenticateWithNonExistingProfessor() {

        var professorEntityTest = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .nomeProfessor("qualquerNomeProfessor")
                .password("SenhaForte123!")
                .build();

        var authProfessorDTO = new AuthProfessorDTO(professorEntityTest.getNomeProfessor(),
                professorEntityTest.getPassword());

        doReturn(Optional.empty()).when(professorRepository)
                .findByNomeProfessor(professorEntityTest.getNomeProfessor());

        Assertions.assertThatThrownBy(() -> authProfessorUseCase.execute(authProfessorDTO))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(professorRepository).findByNomeProfessor(authProfessorDTO.getNomeProfessor());
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("Should not be able to authenticate with incorrect password")
    public void shouldNotBeAbleToAuthenticateWithIncorrectPassword() {

        var professorEntityTest = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .nomeProfessor("qualquerNomeProfessor")
                .password("SenhaForte123!")
                .build();

        var authProfessorDTO = new AuthProfessorDTO(professorEntityTest.getNomeProfessor(),
                professorEntityTest.getPassword());

        doReturn(Optional.of(professorEntityTest)).when(professorRepository)
                .findByNomeProfessor(authProfessorDTO.getNomeProfessor());

        when(passwordEncoder.matches(authProfessorDTO.getPassword(), professorEntityTest.getPassword()))
                .thenReturn(false);

        Assertions.assertThatThrownBy(() -> authProfessorUseCase.execute(authProfessorDTO))
                .isInstanceOf(javax.naming.AuthenticationException.class);

        verify(passwordEncoder).matches(authProfessorDTO.getPassword(), professorEntityTest.getPassword());
    }

    @Test
    @DisplayName("Should be able to authenticate successfully")
    public void shouldBeAbleToAuthenticateSuccessfully() throws AuthenticationException {

        String secret = "minha-chave-teste";
        ReflectionTestUtils.setField(authProfessorUseCase, "secretKey", secret);

        var professorEntityTest = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .nomeProfessor("qualquerNomeProfessor")
                .password("SenhaForte123!")
                .build();

        var authProfessorDTO = new AuthProfessorDTO(professorEntityTest.getNomeProfessor(),
                professorEntityTest.getPassword());

        when(professorRepository.findByNomeProfessor(authProfessorDTO.getNomeProfessor()))
                .thenReturn(Optional.of(professorEntityTest));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        var response = authProfessorUseCase.execute(authProfessorDTO);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getAccess_token()).isNotBlank();
        Assertions.assertThat(response.getExpires_in()).isNotNull();

        DecodedJWT decodedJWT = JWT.decode(response.getAccess_token());

        Assertions.assertThat(decodedJWT.getSubject()).isEqualTo(professorEntityTest.getId().toString());

    }

}
