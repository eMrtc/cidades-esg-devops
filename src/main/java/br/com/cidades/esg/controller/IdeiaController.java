package br.com.cidades.esg.controller;

import br.com.cidades.esg.dto.AtualizarStatusIdeiaDTO;
import br.com.cidades.esg.dto.IdeiaRequestDTO;
import br.com.cidades.esg.dto.IdeiaResponseDTO;
import br.com.cidades.esg.model.IdeiaStatus;
import br.com.cidades.esg.service.IdeiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideias")
@RequiredArgsConstructor
public class IdeiaController {

    private final IdeiaService service;

    @GetMapping
    public ResponseEntity<List<IdeiaResponseDTO>> listar(
            @RequestParam(required = false) IdeiaStatus status,
            Authentication authentication) {

        if (status != null) {
            return ResponseEntity.ok(service.listarPorStatus(status));
        }

        boolean isOperador = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OPERADOR"));

        if (isOperador) {
            return ResponseEntity.ok(service.listarPorOperador(authentication.getName()));
        }

        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdeiaResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<IdeiaResponseDTO> criar(
            @Valid @RequestBody IdeiaRequestDTO dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto, authentication.getName()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IdeiaResponseDTO> atualizarStatus(
            @PathVariable String id,
            @Valid @RequestBody AtualizarStatusIdeiaDTO dto) {
        return ResponseEntity.ok(service.atualizarStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
