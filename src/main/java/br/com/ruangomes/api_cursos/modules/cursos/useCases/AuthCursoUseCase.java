package br.com.ruangomes.api_cursos.modules.cursos.useCases;

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

import br.com.ruangomes.api_cursos.modules.cursos.dto.AuthCursoDTO;
import br.com.ruangomes.api_cursos.modules.cursos.dto.AuthCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;

@Service
public class AuthCursoUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCursoResponseDTO execute(AuthCursoDTO authCursoDTO) throws AuthenticationException {
        var curso = this.cursoRepository.findByName(authCursoDTO.getName()).orElseThrow(() -> {
            throw new UsernameNotFoundException("Name/password incorrect");
        });
        
        var passwordMatches = this.passwordEncoder.matches(authCursoDTO.getPassword(), curso.getPassword());

        if(!passwordMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expires_in = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("cursos")
        .withSubject(curso.getId().toString())
        .withExpiresAt(expires_in)
        .withClaim("roles", Arrays.asList("CURSO"))
        .sign(algorithm);

        var authCursoResponseDTO = AuthCursoResponseDTO.builder()
        .access_token(token)
        .expires_in(expires_in.toEpochMilli())
        .build();
        return authCursoResponseDTO;
    }
}
