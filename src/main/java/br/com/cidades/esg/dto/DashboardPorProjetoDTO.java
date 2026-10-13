package br.com.cidades.esg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardPorProjetoDTO {
    private String idProjeto;
    private String nomeProjeto;
    private String etapa;
    private BigDecimal investimento;
    private BigDecimal retornoFinanceiro;
    private Double aumentoProdutividade;
    private BigDecimal roi;
}
