package br.com.cidades.esg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orientacoes_estrategicas")
public class OrientacaoEstrategica {

    @Id
    private String id;

    private String titulo;

    private String descricao;

    private String categoria; // Ambiental (E), Social (S), Governança (G)

    private String campanha;

    private LocalDate data;

    @Builder.Default
    private boolean ativa = true;
}
