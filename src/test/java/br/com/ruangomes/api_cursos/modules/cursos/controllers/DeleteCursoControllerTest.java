package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.utils.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DeleteCursoControllerTest {

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
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Should delete a curso successfully")
    public void shouldDeleteCursoSuccessfully() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTest")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var cursoEntityTest = CursosEntity.builder()
                .name("CursoTest")
                .category("CategoryTest")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest);

        var result = mvc.perform(MockMvcRequestBuilders.delete("/professor/cursos/{id}", cursoEntityTest.getId())
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @AfterEach
    public void tearDown() {
        cursoRepository.deleteAll();
        professorRepository.deleteAll();

    }

}
