package br.com.ruangomes.api_cursos.modules.professor.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import br.com.ruangomes.api_cursos.modules.professor.dto.AuthProfessorDTO;
import br.com.ruangomes.api_cursos.modules.professor.dto.AuthProfessorResponseDTO;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;

@Service
public class AuthProfessorUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthProfessorResponseDTO execute(AuthProfessorDTO authProfessorDTO) throws AuthenticationException {
        var professor = this.professorRepository.findByNomeProfessor(authProfessorDTO.getNomeProfessor()).orElseThrow(() -> {
            throw new UsernameNotFoundException("Professor/password incorrect");
        });
        
        var passwordMatches = this.passwordEncoder.matches(authProfessorDTO.getPassword(), professor.getPassword());

        if(!passwordMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expires_in = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("cursos")
        .withSubject(professor.getId().toString())
        .withExpiresAt(expires_in)
        .withClaim("roles", Arrays.asList("PROFESSOR"))
        .sign(algorithm);

        var authProfessorResponseDTO = AuthProfessorResponseDTO.builder()
        .access_token(token)
        .expires_in(expires_in.toEpochMilli())
        .build();
        return authProfessorResponseDTO;
    }
}
