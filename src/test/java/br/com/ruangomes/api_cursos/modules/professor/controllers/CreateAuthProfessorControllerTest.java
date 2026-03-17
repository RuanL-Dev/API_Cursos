package br.com.ruangomes.api_cursos.modules.professor.controllers;

import br.com.ruangomes.api_cursos.modules.professor.dto.AuthProfessorDTO;
import br.com.ruangomes.api_cursos.modules.professor.entities.ProfessorEntity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.ruangomes.api_cursos.modules.professor.repositories.ProfessorRepository;
import br.com.ruangomes.api_cursos.utils.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateAuthProfessorControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        professorRepository.deleteAll();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Should authenticate a professor successfully")
    public void shouldAuthenticateAProfessorSuccessfully() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("NomeProfessorTest1")
                .password(passwordEncoder.encode("SenhaForte123!"))
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        AuthProfessorDTO authProfessorDTOTest = new AuthProfessorDTO("NomeProfessorTest1", "SenhaForte123!");

        var result = mvc.perform(MockMvcRequestBuilders.post("/professor/auth")
                .contentType("application/json")
                .content(TestUtils.objectToJson(authProfessorDTOTest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expires_in").exists())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Should fail to authenticate a professor with incorrect password")
    public void shouldFailToAuthenticateAProfessorWithIncorrectPassword() throws Exception {

        var professorEntityTest = ProfessorEntity.builder()
                .nomeProfessor("NomeProfessorTest2")
                .password(passwordEncoder.encode("SenhaForte123!"))
                .build();

        professorRepository.saveAndFlush(professorEntityTest);

        var authProfessorDTOTest = new AuthProfessorDTO("NomeProfessorTest2", "SenhaErrada123!");

        var result = mvc.perform(MockMvcRequestBuilders.post("/professor/auth")
                .contentType("application/json")
                .content(TestUtils.objectToJson(authProfessorDTOTest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());

    }

    @AfterEach
    public void tearDown() {
        professorRepository.deleteAll();
    }

}
