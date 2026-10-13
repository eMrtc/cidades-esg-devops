package br.com.cidades.esg.controller;

import br.com.cidades.esg.dto.ProjetoRequestDTO;
import br.com.cidades.esg.dto.ProjetoResponseDTO;
import br.com.cidades.esg.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService service;

    @GetMapping
    public ResponseEntity<List<ProjetoResponseDTO>> listar(
            @RequestParam(required = false) String idEstrategia,
            Authentication authentication) {

        if (idEstrategia != null && !idEstrategia.isBlank()) {
            return ResponseEntity.ok(service.listarPorEstrategia(idEstrategia));
        }

        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> criar(
            @Valid @RequestBody ProjetoRequestDTO dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ProjetoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
