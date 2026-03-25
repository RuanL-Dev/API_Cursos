package br.com.ruangomes.api_cursos.modules.cursos.controllers;

import java.util.UUID;

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

import br.com.ruangomes.api_cursos.modules.cursos.dto.UpdateCursoRequestDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;
import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.utils.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UpdateCursoControllerTest {

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
    @DisplayName("Should update a course successfully")
    public void shouldUpdateCourseSuccessfully() throws Exception {

        var updateCursoRequestDTOTest = UpdateCursoRequestDTO.builder()
                .name("CursoAtualizado")
                .category("CategoriaAtualizada")
                .professor("ProfessorTest2")
                .build();

        var professorEntityTest1 = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTest1")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest1);

        var professorEntityTest2 = ProfessorEntity.builder()
                .nomeProfessor("ProfessorTest2")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest2);

        var cursoEntityTest = CursosEntity.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .professor(professorEntityTest1)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest);

        mvc.perform(MockMvcRequestBuilders.put("/professor/cursos/{id}", cursoEntityTest.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(updateCursoRequestDTOTest))
                .header("Authorization", TestUtils.generateToken(professorEntityTest1.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updateCursoRequestDTOTest.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(updateCursoRequestDTOTest.getCategory()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.professor").value(updateCursoRequestDTOTest.getProfessor()))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Should return not found when trying to update a course that does not exist")
    public void shouldReturnNotFoundWhenUpdatingNonExistentCourse() throws Exception {
        var updateCursoRequestDTOTest = UpdateCursoRequestDTO.builder()
                .name("CursoAtualizado")
                .category("CategoriaAtualizada")
                .professor("ProfessorAtualizado")
                .build();

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorAtualizado")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var nonExistingCourseId = UUID.randomUUID();

        mvc.perform(MockMvcRequestBuilders.put("/professor/cursos/{id}", nonExistingCourseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(updateCursoRequestDTOTest))
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("No content available/registered"));
    }

    @Test
    @DisplayName("Should return not found when trying to update a course with a non-existent professor")
    public void shouldReturnNotFoundWhenUpdatingCourseWithNonExistentProfessor() throws Exception {

        var updateCursoRequestDTOTest = UpdateCursoRequestDTO.builder()
                .name("CursoAtualizado")
                .category("CategoriaAtualizada")
                .professor("ProfessorInexistente")
                .build();

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorAtualizado")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var cursoEntityTest = CursosEntity.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest);

        mvc.perform(MockMvcRequestBuilders.put("/professor/cursos/{id}", cursoEntityTest.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(updateCursoRequestDTOTest))
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Professor não encontrado."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.field").value("UsernameNotFoundException"));
    }

    @Test
    @DisplayName("Should return bad request when trying to update a course without providing any data")
    public void shouldReturnBadRequestWhenUpdatingCourseWithoutProvidingData() throws Exception {

        var updateCursoRequestDTOTest = UpdateCursoRequestDTO.builder().build();

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("ProfessorAtualizado")
                .password("SenhaForte123!")
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var cursoEntityTest = CursosEntity.builder()
                .name("CursoTeste")
                .category("CategoriaTeste")
                .professor(professorEntityTest)
                .build();

        cursoRepository.saveAndFlush(cursoEntityTest);

        mvc.perform(MockMvcRequestBuilders.put("/professor/cursos/{id}", cursoEntityTest.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(updateCursoRequestDTOTest))
                .header("Authorization", TestUtils.generateToken(professorEntityTest.getId(), secret)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Nenhum dado fornecido para atualização."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.field").value("IllegalArgumentException"));

    }

}
