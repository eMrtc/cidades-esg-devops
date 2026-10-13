package br.com.cidades.esg;

import br.com.cidades.esg.dto.CriarUsuarioDTO;
import br.com.cidades.esg.dto.LoginRequestDTO;
import br.com.cidades.esg.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CidadesEsgIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve registrar um novo usuário Gestor ESG com sucesso")
    void deveRegistrarUsuarioComSucesso() throws Exception {
        CriarUsuarioDTO dto = CriarUsuarioDTO.builder()
                .nome("Gestor Sustentabilidade")
                .email("gestor.esg@cidades.gov.br")
                .senha("Senha@Forte2026")
                .role(Role.GESTOR)
                .build();

        mockMvc.perform(post("/api/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("gestor.esg@cidades.gov.br"))
                .andExpect(jsonPath("$.role").value("GESTOR"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve realizar login e retornar Token JWT válido")
    void deveFazerLoginERetornarToken() throws Exception {
        CriarUsuarioDTO registrar = CriarUsuarioDTO.builder()
                .nome("Lider ESG")
                .email("lider.esg@cidades.gov.br")
                .senha("Senha@Lider2026")
                .role(Role.LIDER)
                .build();

        mockMvc.perform(post("/api/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrar)))
                .andExpect(status().isCreated());

        LoginRequestDTO login = LoginRequestDTO.builder()
                .email("lider.esg@cidades.gov.br")
                .senha("Senha@Lider2026")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.role").value("LIDER"));
    }
}
