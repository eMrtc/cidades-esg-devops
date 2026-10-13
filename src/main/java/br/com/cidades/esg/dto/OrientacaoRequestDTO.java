package br.com.cidades.esg.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrientacaoRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "A categoria ESG é obrigatória (ex: Ambiental, Social, Governança)")
    private String categoria;

    private String campanha;

    private LocalDate data;

    @Builder.Default
    private boolean ativa = true;
}
