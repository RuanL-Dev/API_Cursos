package br.com.ruangomes.api_cursos.modules.professor.controllers;

import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.utils.TestUtils;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateProfessorControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Value("${security.token.secret.test}")
    private String secret;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CursoRepository cursoRepository;

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
    @DisplayName("Should create a new professor successfully")
    public void shouldCreateANewProfessorSuccessfully() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("NomeProfessorTest")
                .password("SenhaForte123!")
                .build();

        var result = mvc.perform(MockMvcRequestBuilders.post("/professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(professorEntityTest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeProfessor").value("NomeProfessorTest"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should return bad request when trying to create a professor with an existing name")
    public void shouldReturnBadRequestWhenTryingToCreateAProfessorWithAnExistingName() throws Exception {
        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("NomeProfessorTest")
                .password("SenhaForte123!")
                .build();

        professorEntityTest = professorRepository.saveAndFlush(professorEntityTest);

        mvc.perform(MockMvcRequestBuilders.post("/professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(professorEntityTest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @AfterEach
    public void tearDown() {
        cursoRepository.deleteAll();
        professorRepository.deleteAll();

    }

}
