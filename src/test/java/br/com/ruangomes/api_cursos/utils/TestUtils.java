package br.com.ruangomes.api_cursos.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static final String objectToJson(Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateToken(UUID idProfessor, String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expires_in = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("cursos")
                .withSubject(idProfessor.toString())
                .withExpiresAt(expires_in)
                .withClaim("roles", Arrays.asList("PROFESSOR"))
                .sign(algorithm);

        return token;
    }
}
