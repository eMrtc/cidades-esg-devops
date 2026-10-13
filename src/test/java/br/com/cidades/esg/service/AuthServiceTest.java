package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.CriarUsuarioDTO;
import br.com.cidades.esg.dto.UsuarioResponseDTO;
import br.com.cidades.esg.exception.RegraDeNegocioException;
import br.com.cidades.esg.model.Role;
import br.com.cidades.esg.model.Usuario;
import br.com.cidades.esg.repository.UsuarioRepository;
import br.com.cidades.esg.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private CriarUsuarioDTO criarUsuarioDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        criarUsuarioDTO = CriarUsuarioDTO.builder()
                .nome("Catarina Costa")
                .email("catarina.devops@esg.com.br")
                .senha("Senha@12345")
                .role(Role.GESTOR)
                .build();

        usuario = Usuario.builder()
                .id("670ab34c123456")
                .nome("Catarina Costa")
                .email("catarina.devops@esg.com.br")
                .senha("hash_senha_bcrypt")
                .role(Role.GESTOR)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void deveRegistrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail("catarina.devops@esg.com.br")).thenReturn(false);
        when(passwordEncoder.encode("Senha@12345")).thenReturn("hash_senha_bcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO response = authService.registrar(criarUsuarioDTO);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("catarina.devops@esg.com.br");
        assertThat(response.getRole()).isEqualTo(Role.GESTOR);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar e-mail já existente")
    void deveLancarExcecaoAoCadastrarEmailExistente() {
        when(usuarioRepository.existsByEmail("catarina.devops@esg.com.br")).thenReturn(true);

        assertThatThrownBy(() -> authService.registrar(criarUsuarioDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("E-mail já cadastrado no sistema.");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
