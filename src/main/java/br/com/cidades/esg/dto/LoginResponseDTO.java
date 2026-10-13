package br.com.cidades.esg.dto;

import br.com.cidades.esg.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private String id;
    private String nome;
    private String email;
    private Role role;
    private Long expiresIn;
}
