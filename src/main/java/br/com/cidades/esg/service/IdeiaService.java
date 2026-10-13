package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.AtualizarStatusIdeiaDTO;
import br.com.cidades.esg.dto.IdeiaRequestDTO;
import br.com.cidades.esg.dto.IdeiaResponseDTO;
import br.com.cidades.esg.exception.RecursoNaoEncontradoException;
import br.com.cidades.esg.integration.GeminiClient;
import br.com.cidades.esg.model.Ideia;
import br.com.cidades.esg.model.IdeiaStatus;
import br.com.cidades.esg.model.OrientacaoEstrategica;
import br.com.cidades.esg.model.Usuario;
import br.com.cidades.esg.repository.IdeiaRepository;
import br.com.cidades.esg.repository.OrientacaoEstrategicaRepository;
import br.com.cidades.esg.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeiaService {

    private final IdeiaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final OrientacaoEstrategicaRepository orientacaoRepository;
    private final GeminiClient geminiClient;

    public List<IdeiaResponseDTO> listarTodas() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<IdeiaResponseDTO> listarPorOperador(String emailOperador) {
        Usuario usuario = usuarioRepository.findByEmail(emailOperador)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + emailOperador));
        return repository.findByIdOperador(usuario.getId()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<IdeiaResponseDTO> listarPorStatus(IdeiaStatus status) {
        return repository.findByStatus(status).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public IdeiaResponseDTO buscarPorId(String id) {
        Ideia ideia = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ideia ESG não encontrada com ID: " + id));
        return mapToDTO(ideia);
    }

    public IdeiaResponseDTO criar(IdeiaRequestDTO dto, String emailOperador) {
        Usuario usuario = usuarioRepository.findByEmail(emailOperador)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + emailOperador));

        String contextoEstrategia = "";
        if (dto.getIdEstrategia() != null && !dto.getIdEstrategia().isBlank()) {
            contextoEstrategia = orientacaoRepository.findById(dto.getIdEstrategia())
                    .map(OrientacaoEstrategica::getTitulo)
                    .orElse("");
        }

        Integer pontuacao = null;
        String justificativa = null;

        if (geminiClient.isConfigurado()) {
            try {
                String prompt = String.format(
                        "Você é um consultor especialista em ESG e Cidades Inteligentes. Avalie a seguinte iniciativa ESG:\n" +
                        "Título: %s\nDescrição: %s\nContexto da Meta Estratégica: %s\n\n" +
                        "Responda EXATAMENTE no formato:\nPONTUACAO: [numero de 0 a 100]\nJUSTIFICATIVA: [uma frase concisa sobre viabilidade e impacto sustentavel]",
                        dto.getTitulo(), dto.getDescricao(), contextoEstrategia
                );
                String respostaIA = geminiClient.enviarPrompt(prompt);
                pontuacao = extrairPontuacao(respostaIA);
                justificativa = extrairJustificativa(respostaIA);
            } catch (Exception e) {
                log.warn("Falha ao avaliar ideia com IA: {}", e.getMessage());
            }
        }

        Ideia ideia = Ideia.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status(IdeiaStatus.PENDENTE)
                .idEstrategia(dto.getIdEstrategia())
                .idOperador(usuario.getId())
                .nomeOperador(usuario.getNome())
                .pontuacaoIA(pontuacao)
                .justificativaIA(justificativa)
                .build();

        return mapToDTO(repository.save(ideia));
    }

    public IdeiaResponseDTO atualizarStatus(String id, AtualizarStatusIdeiaDTO dto) {
        Ideia ideia = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ideia ESG não encontrada com ID: " + id));

        ideia.setStatus(dto.getStatus());
        return mapToDTO(repository.save(ideia));
    }

    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Ideia ESG não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    private Integer extrairPontuacao(String texto) {
        try {
            for (String linha : texto.split("\n")) {
                if (linha.toUpperCase().startsWith("PONTUACAO:")) {
                    String num = linha.replaceAll("[^0-9]", "").trim();
                    if (!num.isEmpty()) {
                        int val = Integer.parseInt(num);
                        return Math.min(100, Math.max(0, val));
                    }
                }
            }
        } catch (Exception ignored) {}
        return 75; // Valor padrão
    }

    private String extrairJustificativa(String texto) {
        try {
            for (String linha : texto.split("\n")) {
                if (linha.toUpperCase().startsWith("JUSTIFICATIVA:")) {
                    return linha.substring("JUSTIFICATIVA:".length()).trim();
                }
            }
        } catch (Exception ignored) {}
        return "Iniciativa relevante com potencial impacto positivo na sustentabilidade e eficiência urbana.";
    }

    private IdeiaResponseDTO mapToDTO(Ideia i) {
        return IdeiaResponseDTO.builder()
                .id(i.getId())
                .titulo(i.getTitulo())
                .descricao(i.getDescricao())
                .status(i.getStatus())
                .idEstrategia(i.getIdEstrategia())
                .idOperador(i.getIdOperador())
                .nomeOperador(i.getNomeOperador())
                .dataCriacao(i.getDataCriacao())
                .pontuacaoIA(i.getPontuacaoIA())
                .justificativaIA(i.getJustificativaIA())
                .build();
    }
}
