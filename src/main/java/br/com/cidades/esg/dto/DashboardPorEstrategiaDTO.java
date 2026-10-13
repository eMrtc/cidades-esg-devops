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
public class DashboardPorEstrategiaDTO {
    private String idEstrategia;
    private String tituloEstrategia;
    private String categoria;
    private long totalIdeias;
    private long totalProjetos;
    private BigDecimal investimentoTotal;
}
