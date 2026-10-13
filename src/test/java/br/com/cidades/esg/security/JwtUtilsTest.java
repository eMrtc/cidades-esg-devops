package br.com.cidades.esg.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String secret = "testSecretKeyForUnitTestsMustBe256BitsLongMinimumRequiredForCidadesESG2026";
    private final long expirationMs = 3600000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(secret, expirationMs);
    }

    @Test
    @DisplayName("Deve gerar token JWT e extrair email corretamente")
    void deveGerarETrairEmail() {
        UserDetails user = new User("admin@cidades.esg.gov.br", "password",
                List.of(new SimpleGrantedAuthority("ROLE_GESTOR")));

        String token = jwtUtils.gerarToken(user);

        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtUtils.validarToken(token)).isTrue();
        assertThat(jwtUtils.extrairEmail(token)).isEqualTo("admin@cidades.esg.gov.br");
    }

    @Test
    @DisplayName("Deve invalidar token adulterado")
    void deveInvalidarTokenAdulterado() {
        UserDetails user = new User("admin@cidades.esg.gov.br", "password",
                List.of(new SimpleGrantedAuthority("ROLE_GESTOR")));

        String token = jwtUtils.gerarToken(user);
        String tokenAdulterado = token + "modificado";

        assertThat(jwtUtils.validarToken(tokenAdulterado)).isFalse();
    }
}
