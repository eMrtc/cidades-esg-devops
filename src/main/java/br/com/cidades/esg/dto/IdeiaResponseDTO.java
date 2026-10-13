package br.com.cidades.esg.dto;

import br.com.cidades.esg.model.IdeiaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdeiaResponseDTO {
    private String id;
    private String titulo;
    private String descricao;
    private IdeiaStatus status;
    private String idEstrategia;
    private String idOperador;
    private String nomeOperador;
    private LocalDateTime dataCriacao;
    private Integer pontuacaoIA;
    private String justificativaIA;
}
