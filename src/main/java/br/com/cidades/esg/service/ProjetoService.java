package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.ProjetoRequestDTO;
import br.com.cidades.esg.dto.ProjetoResponseDTO;
import br.com.cidades.esg.exception.RecursoNaoEncontradoException;
import br.com.cidades.esg.model.Projeto;
import br.com.cidades.esg.model.Usuario;
import br.com.cidades.esg.repository.ProjetoRepository;
import br.com.cidades.esg.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public List<ProjetoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ProjetoResponseDTO> listarPorGestor(String emailGestor) {
        Usuario usuario = usuarioRepository.findByEmail(emailGestor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + emailGestor));
        return repository.findByIdGestor(usuario.getId()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ProjetoResponseDTO> listarPorEstrategia(String idEstrategia) {
        return repository.findByIdEstrategia(idEstrategia).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ProjetoResponseDTO buscarPorId(String id) {
        Projeto projeto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto ESG não encontrado com ID: " + id));
        return mapToDTO(projeto);
    }

    public ProjetoResponseDTO criar(ProjetoRequestDTO dto, String emailGestor) {
        Usuario usuario = usuarioRepository.findByEmail(emailGestor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + emailGestor));

        Projeto projeto = Projeto.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .etapa(dto.getEtapa())
                .investimento(dto.getInvestimento())
                .prazo(dto.getPrazo())
                .retornoFinanceiro(dto.getRetornoFinanceiro())
                .aumentoProdutividade(dto.getAumentoProdutividade())
                .idEstrategia(dto.getIdEstrategia())
                .idGestor(usuario.getId())
                .nomeGestor(usuario.getNome())
                .build();

        return mapToDTO(repository.save(projeto));
    }

    public ProjetoResponseDTO atualizar(String id, ProjetoRequestDTO dto) {
        Projeto projeto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto ESG não encontrado com ID: " + id));

        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());
        projeto.setEtapa(dto.getEtapa());
        projeto.setInvestimento(dto.getInvestimento());
        projeto.setPrazo(dto.getPrazo());
        projeto.setRetornoFinanceiro(dto.getRetornoFinanceiro());
        projeto.setAumentoProdutividade(dto.getAumentoProdutividade());
        projeto.setIdEstrategia(dto.getIdEstrategia());

        return mapToDTO(repository.save(projeto));
    }

    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Projeto ESG não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private ProjetoResponseDTO mapToDTO(Projeto p) {
        return ProjetoResponseDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .descricao(p.getDescricao())
                .etapa(p.getEtapa())
                .investimento(p.getInvestimento())
                .prazo(p.getPrazo())
                .retornoFinanceiro(p.getRetornoFinanceiro())
                .aumentoProdutividade(p.getAumentoProdutividade())
                .idEstrategia(p.getIdEstrategia())
                .idGestor(p.getIdGestor())
                .nomeGestor(p.getNomeGestor())
                .build();
    }
}
