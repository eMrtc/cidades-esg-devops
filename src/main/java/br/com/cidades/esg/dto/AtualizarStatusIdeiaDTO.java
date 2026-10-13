package br.com.cidades.esg.dto;

import br.com.cidades.esg.model.IdeiaStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusIdeiaDTO {

    @NotNull(message = "O novo status é obrigatório")
    private IdeiaStatus status;
}
