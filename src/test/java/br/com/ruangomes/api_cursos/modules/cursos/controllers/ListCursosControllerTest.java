package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ListCursosControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Value("${security.token.secret.test}")
    private String secret;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @BeforeEach
    public void setup() {

        cursoRepository.deleteAll();
        professorRepository.deleteAll();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("")

}
