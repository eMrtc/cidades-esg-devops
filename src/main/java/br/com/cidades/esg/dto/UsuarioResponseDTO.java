package br.com.cidades.esg.dto;

import br.com.cidades.esg.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private String id;
    private String nome;
    private String email;
    private Role role;
    private boolean ativo;
    private LocalDateTime criadoEm;
}
