package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import static org.mockito.ArgumentMatchers.isNull;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
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
    @DisplayName("Should return a list of courses successfully")
    public void shouldReturnListOfCoursesSuccessfully() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTest")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var cursoEntityTest1 = CursosEntity.builder()
                .name("CursoTeste1")
                .category("CategoriaTeste")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest1);

        var cursoEntityTest2 = CursosEntity.builder()
                .name("CursoTeste2")
                .category("CategoriaTeste")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest2);

        mvc.perform(MockMvcRequestBuilders.get("/professor/cursos")
                .param("category", "CategoriaTeste")
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name")
                        .value(Matchers.containsInAnyOrder("CursoTeste1", "CursoTeste2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].category")
                        .value(Matchers.everyItem(Matchers.is("CategoriaTeste"))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should return no content when there are no courses")
    public void shouldReturnNoContentWhenThereAreNoCourses() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTest")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        Assertions.assertThat(cursoRepository.findAll().isEmpty());

        mvc.perform(MockMvcRequestBuilders.get("/professor/cursos")
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));

    }

}
