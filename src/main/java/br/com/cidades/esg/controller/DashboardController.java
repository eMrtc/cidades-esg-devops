package br.com.cidades.esg.controller;

import br.com.cidades.esg.dto.DashboardResponseDTO;
import br.com.cidades.esg.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> obterDashboard() {
        return ResponseEntity.ok(service.obterDashboard());
    }
}
