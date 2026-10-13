package br.com.cidades.esg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projetos")
public class Projeto {

    @Id
    private String id;

    private String nome;

    private String descricao;

    private String etapa; // Planejamento, Desenvolvimento, Piloto, Concluído

    private BigDecimal investimento; // Valor em R$

    private LocalDate prazo;

    private BigDecimal retornoFinanceiro; // Economia gerada ou retorno financeiro em R$

    private Double aumentoProdutividade; // % de redução de emissão CO2 / ganho de eficiência

    private String idEstrategia;

    private String idGestor;

    private String nomeGestor;
}
