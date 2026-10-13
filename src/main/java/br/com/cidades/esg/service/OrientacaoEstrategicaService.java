package br.com.cidades.esg.service;

import br.com.cidades.esg.dto.OrientacaoRequestDTO;
import br.com.cidades.esg.dto.OrientacaoResponseDTO;
import br.com.cidades.esg.exception.RecursoNaoEncontradoException;
import br.com.cidades.esg.model.OrientacaoEstrategica;
import br.com.cidades.esg.repository.OrientacaoEstrategicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrientacaoEstrategicaService {

    private final OrientacaoEstrategicaRepository repository;

    public List<OrientacaoResponseDTO> listarTodas() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<OrientacaoResponseDTO> listarAtivas() {
        return repository.findByAtivaTrue().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public OrientacaoResponseDTO buscarPorId(String id) {
        OrientacaoEstrategica orientacao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orientação estratégica ESG não encontrada com ID: " + id));
        return mapToDTO(orientacao);
    }

    public OrientacaoResponseDTO criar(OrientacaoRequestDTO dto) {
        OrientacaoEstrategica orientacao = OrientacaoEstrategica.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .categoria(dto.getCategoria())
                .campanha(dto.getCampanha())
                .data(dto.getData())
                .ativa(dto.isAtiva())
                .build();
        return mapToDTO(repository.save(orientacao));
    }

    public OrientacaoResponseDTO atualizar(String id, OrientacaoRequestDTO dto) {
        OrientacaoEstrategica orientacao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orientação estratégica ESG não encontrada com ID: " + id));

        orientacao.setTitulo(dto.getTitulo());
        orientacao.setDescricao(dto.getDescricao());
        orientacao.setCategoria(dto.getCategoria());
        orientacao.setCampanha(dto.getCampanha());
        orientacao.setData(dto.getData());
        orientacao.setAtiva(dto.isAtiva());

        return mapToDTO(repository.save(orientacao));
    }

    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Orientação estratégica ESG não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    private OrientacaoResponseDTO mapToDTO(OrientacaoEstrategica o) {
        return OrientacaoResponseDTO.builder()
                .id(o.getId())
                .titulo(o.getTitulo())
                .descricao(o.getDescricao())
                .categoria(o.getCategoria())
                .campanha(o.getCampanha())
                .data(o.getData())
                .ativa(o.isAtiva())
                .build();
    }
}
