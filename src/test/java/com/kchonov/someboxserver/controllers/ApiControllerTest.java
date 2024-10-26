package com.kchonov.someboxserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kchonov.someboxserver.config.SecurityConfig;
import com.kchonov.someboxserver.controllers.v1.ApiController;
import com.kchonov.someboxserver.services.FilesService;
import com.kchonov.someboxserver.services.MovieEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
@Import(SecurityConfig.class)
public class ApiControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockBean
    private MovieEntityService movieEntityService;

    @MockBean
    private FilesService filesService;

    @MockBean
    private JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username="testUser", authorities = {"ROLE_USER"})
    void canFetchListOfMovies() throws Exception {
        mockMvc.perform(get("/api/v1/list")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="testUser", authorities = {"ROLE_TEST"})
    void cannotFetchListOfMovies() throws Exception {
        mockMvc.perform(get("/api/v1/list")).andExpect(status().isForbidden());
    }
}
