package br.com.cidades.esg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoResponseDTO {
    private String id;
    private String nome;
    private String descricao;
    private String etapa;
    private BigDecimal investimento;
    private LocalDate prazo;
    private BigDecimal retornoFinanceiro;
    private Double aumentoProdutividade;
    private String idEstrategia;
    private String idGestor;
    private String nomeGestor;
}
