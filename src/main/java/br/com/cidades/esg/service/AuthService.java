package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.CriarUsuarioDTO;
import br.com.cidades.esg.dto.LoginRequestDTO;
import br.com.cidades.esg.dto.LoginResponseDTO;
import br.com.cidades.esg.dto.UsuarioResponseDTO;
import br.com.cidades.esg.exception.RegraDeNegocioException;
import br.com.cidades.esg.model.Usuario;
import br.com.cidades.esg.repository.UsuarioRepository;
import br.com.cidades.esg.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public UsuarioResponseDTO registrar(CriarUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RegraDeNegocioException("E-mail já cadastrado no sistema.");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(dto.getRole())
                .ativo(true)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        return mapToDTO(salvo);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtUtils.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .expiresIn(jwtUtils.getExpirationMs())
                .build();
    }

    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .role(u.getRole())
                .ativo(u.isAtivo())
                .criadoEm(u.getCriadoEm())
                .build();
    }
}
