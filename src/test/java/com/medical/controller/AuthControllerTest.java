package com.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.dto.LoginRequestDTO;
import com.medical.dto.LoginResponseDTO;
import com.medical.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour AuthController.
 * Utilise MockMvc pour tester les endpoints REST sans démarrer le serveur.
 *
 * @SpringBootTest: Crée le contexte Spring complet
 * @AutoConfigureMockMvc: Configure MockMvc pour les tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthController Integration Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequestDTO loginRequest;
    private LoginResponseDTO loginResponse;

    /**
     * Initialisation avant chaque test
     */
    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDTO("doctor_001", "password123");

        loginResponse = LoginResponseDTO.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkb2N0b3JfMDAxIiwicm9sZXMiOlsiUk9MRV9ET0NUT1IiXSwiaWF0IjoxNjIxNTU5NjAwLCJleHAiOjE2MjE2NDYwMDB9.signature")
                .username("doctor_001")
                .email("doctor@medical.com")
                .roles("ROLE_DOCTOR")
                .type("Bearer")
                .build();
    }

    /**
     * Test: Connexion avec succès
     */
    @Test
    @DisplayName("Connexion avec identifiants valides")
    void testLogin_Success() throws Exception {
        // Given: Le service retourne un token
        when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(loginResponse);

        // When: Appeler le endpoint login
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))

                // Then: Vérifier la réponse
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.username").value("doctor_001"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andReturn();

        // Afficher la réponse
        System.out.println("Réponse: " + result.getResponse().getContentAsString());
    }

    /**
     * Test: Connexion avec identifiants incorrects
     */
    @Test
    @DisplayName("Connexion avec identifiants incorrects")
    void testLogin_BadCredentials() throws Exception {
        // Given: Le service lève une exception pour les mauvais identifiants
        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        // When: Appeler le endpoint login
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))

                // Then: Vérifier qu'une erreur 401 est retournée
                .andExpect(status().isUnauthorized());
    }
}

