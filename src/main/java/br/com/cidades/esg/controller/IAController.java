package br.com.cidades.esg.controller;

import br.com.cidades.esg.dto.ChatRequestDTO;
import br.com.cidades.esg.service.ChatIAService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
public class IAController {

    private final ChatIAService chatIAService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@Valid @RequestBody ChatRequestDTO dto) {
        return ResponseEntity.ok(chatIAService.conversar(dto));
    }
}
