package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import java.util.UUID;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
public class PatchCursosControllerTest {

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
    @DisplayName("Should patch a curso successfully")
    public void shouldPatchCursoSuccessfully() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTeste")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var cursoEntityTest = CursosEntity.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest);

        mvc.perform(MockMvcRequestBuilders.patch("/professor/cursos/{id}/active", cursoEntityTest.getId())
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(false))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Should not be able to patch a curso with non existing id")
    public void shouldNotBeAbleToPatchCursoWithNonExistingId() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTeste")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var nonExistingCourseId = UUID.randomUUID();

        mvc.perform(MockMvcRequestBuilders.patch("/professor/cursos/{id}/active", nonExistingCourseId)
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("No content available/registered"));
    }

}
