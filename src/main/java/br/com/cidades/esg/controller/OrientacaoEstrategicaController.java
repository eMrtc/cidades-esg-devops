package br.com.cidades.esg.controller;

import br.com.cidades.esg.dto.OrientacaoRequestDTO;
import br.com.cidades.esg.dto.OrientacaoResponseDTO;
import br.com.cidades.esg.service.OrientacaoEstrategicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orientacoes")
@RequiredArgsConstructor
public class OrientacaoEstrategicaController {

    private final OrientacaoEstrategicaService service;

    @GetMapping
    public ResponseEntity<List<OrientacaoResponseDTO>> listarTodas(@RequestParam(required = false, defaultValue = "false") boolean apenasAtivas) {
        if (apenasAtivas) {
            return ResponseEntity.ok(service.listarAtivas());
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrientacaoResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrientacaoResponseDTO> criar(@Valid @RequestBody OrientacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrientacaoResponseDTO> atualizar(@PathVariable String id, @Valid @RequestBody OrientacaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
