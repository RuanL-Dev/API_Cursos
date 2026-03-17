package br.com.ruangomes.api_cursos.modules.cursos.controllers;

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

import br.com.ruangomes.api_cursos.modules.cursos.dto.CreateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.utils.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateCursoControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProfessorRepository professorRepository;

    @Value("${security.token.secret.test}")
    private String secret;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Should create a new course successfully")
    public void shouldCreateANewCourseSuccessfully() throws Exception {

        var CreateCursoRequestDTOTest = CreateCursoRequestDTO.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .build();

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("NomeProfessorTest")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var result = mvc.perform(MockMvcRequestBuilders.post("/professor/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(CreateCursoRequestDTOTest))
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("CursoTeste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("CategoriaTeste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.professor").value("NomeProfessorTest"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Should return bad request when trying to create a course with an existing name")
    public void shouldReturnBadRequestWhenTryingToCreateACourseWithAnExistingName() throws Exception {

        var CreateCursoRequestDTOTest = CreateCursoRequestDTO.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .build();

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("NomeProfessorTest")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var cursosEntityTest = CursosEntity.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursosEntityTest);

        var result = mvc.perform(MockMvcRequestBuilders.post("/professor/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(CreateCursoRequestDTOTest))
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
