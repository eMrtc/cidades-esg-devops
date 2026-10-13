package br.com.cidades.esg.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProjetoRequestDTO {

    @NotBlank(message = "O nome do projeto é obrigatório")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "A etapa é obrigatória")
    private String etapa;

    @NotNull(message = "O valor de investimento é obrigatório")
    @DecimalMin(value = "0.0", message = "O investimento deve ser positivo")
    private BigDecimal investimento;

    private LocalDate prazo;

    private BigDecimal retornoFinanceiro;

    private Double aumentoProdutividade;

    private String idEstrategia;
}
