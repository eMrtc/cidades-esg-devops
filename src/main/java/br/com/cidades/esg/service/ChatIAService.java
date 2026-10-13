package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.ChatRequestDTO;
import br.com.cidades.esg.integration.GeminiClient;
import br.com.cidades.esg.model.OrientacaoEstrategica;
import br.com.cidades.esg.model.Projeto;
import br.com.cidades.esg.repository.OrientacaoEstrategicaRepository;
import br.com.cidades.esg.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatIAService {

    private final GeminiClient geminiClient;
    private final ProjetoRepository projetoRepository;
    private final OrientacaoEstrategicaRepository orientacaoRepository;

    public Map<String, String> conversar(ChatRequestDTO dto) {
        if (!geminiClient.isConfigurado()) {
            return Map.of(
                    "resposta", "O serviço de IA não está ativo no momento (chave GEMINI_API_KEY não configurada).",
                    "status", "SEM_CHAVE_CONFIGURADA"
            );
        }

        List<Projeto> projetos = projetoRepository.findAll();
        List<OrientacaoEstrategica> orientacoes = orientacaoRepository.findAll();

        String resumoProjetos = projetos.stream()
                .map(p -> String.format("- %s (Etapa: %s, Investimento: R$ %s, Retorno: R$ %s, Eficiência: %.1f%%)",
                        p.getNome(), p.getEtapa(), p.getInvestimento(), p.getRetornoFinanceiro(),
                        p.getAumentoProdutividade() != null ? p.getAumentoProdutividade() : 0.0))
                .collect(Collectors.joining("\n"));

        String resumoMetas = orientacoes.stream()
                .map(o -> String.format("- %s [%s]: %s", o.getTitulo(), o.getCategoria(), o.getDescricao()))
                .collect(Collectors.joining("\n"));

        String promptCompleto = String.format(
                "Você é o Assistente Virtual de Inteligência Artificial para Gestão Estratégica de Cidades ESG Inteligentes.\n" +
                "Você auxilia gestores públicos e líderes a tomarem decisões sobre sustentabilidade, mobilidade urbana verde e eficiência.\n\n" +
                "--- METAS ESTRATÉGICAS ESG ---\n%s\n\n" +
                "--- PROJETOS URBANOS ATIVOS ---\n%s\n\n" +
                "Contexto adicional do usuário: %s\n" +
                "Pergunta do usuário: %s\n\n" +
                "Por favor, responda de forma clara, técnica, executiva e focada em resultados ESG e retorno sustentável.",
                resumoMetas.isEmpty() ? "Nenhuma meta cadastrada." : resumoMetas,
                resumoProjetos.isEmpty() ? "Nenhum projeto cadastrado." : resumoProjetos,
                dto.getContexto() != null ? dto.getContexto() : "Nenhum",
                dto.getMensagem()
        );

        String resposta = geminiClient.enviarPrompt(promptCompleto);
        return Map.of(
                "resposta", resposta.isEmpty() ? "Não foi possível obter resposta do modelo de IA neste momento." : resposta,
                "status", "SUCESSO"
        );
    }
}
