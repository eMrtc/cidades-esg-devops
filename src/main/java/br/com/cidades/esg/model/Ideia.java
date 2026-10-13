package br.com.cidades.esg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ideias")
public class Ideia {

    @Id
    private String id;

    private String titulo;

    private String descricao;

    @Builder.Default
    private IdeiaStatus status = IdeiaStatus.PENDENTE;

    private String idEstrategia;

    private String idOperador;

    private String nomeOperador;

    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    private Integer pontuacaoIA;

    private String justificativaIA;
}
