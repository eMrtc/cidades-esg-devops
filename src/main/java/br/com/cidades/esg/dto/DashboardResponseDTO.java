package br.com.cidades.esg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private long totalIdeias;
    private long ideiasAprovadas;
    private long ideiasPendentes;
    private long totalProjetos;
    private BigDecimal investimentoTotal;
    private BigDecimal retornoEstimadoTotal;
    private Double mediaAumentoProdutividade;
    private List<DashboardPorEstrategiaDTO> porEstrategia;
    private List<DashboardPorProjetoDTO> porProjeto;
    private String analiseIA;
}
