package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.DashboardPorEstrategiaDTO;
import br.com.cidades.esg.dto.DashboardPorProjetoDTO;
import br.com.cidades.esg.dto.DashboardResponseDTO;
import br.com.cidades.esg.integration.GeminiClient;
import br.com.cidades.esg.model.Ideia;
import br.com.cidades.esg.model.IdeiaStatus;
import br.com.cidades.esg.model.OrientacaoEstrategica;
import br.com.cidades.esg.model.Projeto;
import br.com.cidades.esg.repository.IdeiaRepository;
import br.com.cidades.esg.repository.OrientacaoEstrategicaRepository;
import br.com.cidades.esg.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IdeiaRepository ideiaRepository;
    private final ProjetoRepository projetoRepository;
    private final OrientacaoEstrategicaRepository orientacaoRepository;
    private final GeminiClient geminiClient;

    public DashboardResponseDTO obterDashboard() {
        List<Ideia> todasIdeias = ideiaRepository.findAll();
        List<Projeto> todosProjetos = projetoRepository.findAll();
        List<OrientacaoEstrategica> orientacoes = orientacaoRepository.findAll();

        long totalIdeias = todasIdeias.size();
        long aprovadas = todasIdeias.stream().filter(i -> i.getStatus() == IdeiaStatus.APROVADA).count();
        long pendentes = todasIdeias.stream().filter(i -> i.getStatus() == IdeiaStatus.PENDENTE).count();

        BigDecimal investimentoTotal = todosProjetos.stream()
                .map(Projeto::getInvestimento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal retornoTotal = todosProjetos.stream()
                .map(p -> p.getRetornoFinanceiro() != null ? p.getRetornoFinanceiro() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double mediaProdutividade = todosProjetos.stream()
                .filter(p -> p.getAumentoProdutividade() != null)
                .mapToDouble(Projeto::getAumentoProdutividade)
                .average()
                .orElse(0.0);

        List<DashboardPorEstrategiaDTO> porEstrategia = new ArrayList<>();
        for (OrientacaoEstrategica oe : orientacoes) {
            long ideiasDaEstrategia = todasIdeias.stream()
                    .filter(i -> oe.getId().equals(i.getIdEstrategia()))
                    .count();
            List<Projeto> projetosDaEstrategia = todosProjetos.stream()
                    .filter(p -> oe.getId().equals(p.getIdEstrategia()))
                    .toList();
            BigDecimal investEstrategia = projetosDaEstrategia.stream()
                    .map(Projeto::getInvestimento)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            porEstrategia.add(DashboardPorEstrategiaDTO.builder()
                    .idEstrategia(oe.getId())
                    .tituloEstrategia(oe.getTitulo())
                    .categoria(oe.getCategoria())
                    .totalIdeias(ideiasDaEstrategia)
                    .totalProjetos(projetosDaEstrategia.size())
                    .investimentoTotal(investEstrategia)
                    .build());
        }

        List<DashboardPorProjetoDTO> porProjeto = new ArrayList<>();
        for (Projeto p : todosProjetos) {
            BigDecimal roi = BigDecimal.ZERO;
            if (p.getInvestimento() != null && p.getInvestimento().compareTo(BigDecimal.ZERO) > 0 && p.getRetornoFinanceiro() != null) {
                roi = p.getRetornoFinanceiro().subtract(p.getInvestimento())
                        .divide(p.getInvestimento(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            porProjeto.add(DashboardPorProjetoDTO.builder()
                    .idProjeto(p.getId())
                    .nomeProjeto(p.getNome())
                    .etapa(p.getEtapa())
                    .investimento(p.getInvestimento())
                    .retornoFinanceiro(p.getRetornoFinanceiro())
                    .aumentoProdutividade(p.getAumentoProdutividade())
                    .roi(roi)
                    .build());
        }

        String analiseIA = "Análise de IA disponível quando a chave da API estiver configurada.";
        if (geminiClient.isConfigurado() && !todosProjetos.isEmpty()) {
            try {
                String prompt = String.format(
                        "Você é um especialista em ESG e Cidades Inteligentes. Analise o seguinte cenário municipal:\n" +
                        "- Total de Iniciativas: %d (Aprovadas: %d, Pendentes: %d)\n" +
                        "- Total de Projetos Urbanos: %d\n" +
                        "- Investimento Total: R$ %s\n" +
                        "- Retorno/Economia Sustentável Estimada: R$ %s\n" +
                        "- Média de Eficiência / Redução de Emissões: %.1f%%\n\n" +
                        "Forneça um parágrafo conciso com insights estratégicos de sustentabilidade e próximos passos recomendados.",
                        totalIdeias, aprovadas, pendentes, todosProjetos.size(),
                        investimentoTotal, retornoTotal, mediaProdutividade
                );
                analiseIA = geminiClient.enviarPrompt(prompt);
            } catch (Exception e) {
                log.warn("Erro ao gerar análise do dashboard com IA: {}", e.getMessage());
            }
        }

        return DashboardResponseDTO.builder()
                .totalIdeias(totalIdeias)
                .ideiasAprovadas(aprovadas)
                .ideiasPendentes(pendentes)
                .totalProjetos(todosProjetos.size())
                .investimentoTotal(investimentoTotal)
                .retornoEstimadoTotal(retornoTotal)
                .mediaAumentoProdutividade(mediaProdutividade)
                .porEstrategia(porEstrategia)
                .porProjeto(porProjeto)
                .analiseIA(analiseIA)
                .build();
    }
}
