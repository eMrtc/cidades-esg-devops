package br.com.cidades.esg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrientacaoResponseDTO {
    private String id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String campanha;
    private LocalDate data;
    private boolean ativa;
}
