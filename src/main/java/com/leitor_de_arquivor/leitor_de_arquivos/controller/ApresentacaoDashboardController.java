package com.leitor_de_arquivor.leitor_de_arquivos.controller;

import com.leitor_de_arquivor.leitor_de_arquivos.model.ApresentacaoDashboard;
import com.leitor_de_arquivor.leitor_de_arquivos.repository.ApresentacaoDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/dashboard")
public class ApresentacaoDashboardController {

    @Autowired
    private ApresentacaoDashboardRepository dashboardRepository;

    @PostMapping("/salvar")
    public ResponseEntity<ApresentacaoDashboard> salvarDashboard(@RequestBody ApresentacaoDashboard dashboard) {
        dashboard.setCriadoEm(LocalDateTime.now());
        ApresentacaoDashboard salvo = dashboardRepository.save(dashboard);

        return ResponseEntity.ok(salvo);

    }

    @PostMapping("/salvar-lista")
    public ResponseEntity<ApresentacaoDashboard[]> salvarArrayDashboard(@RequestBody ApresentacaoDashboard[] dashboards) {
        ApresentacaoDashboard[] returnDashboards = new ApresentacaoDashboard[dashboards.length];
        for(int i = 0; i < dashboards.length; i++){
            dashboards[i].setCriadoEm(LocalDateTime.now());
            returnDashboards[i] = dashboardRepository.save(dashboards[i]);
        }

        return ResponseEntity.ok(returnDashboards);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ApresentacaoDashboard>> listarTodos() {
        return ResponseEntity.ok(dashboardRepository.findAll());
    }

    @GetMapping("/publicos")
    public ResponseEntity<List<ApresentacaoDashboard>> listarPublicos() {
        return ResponseEntity.ok(dashboardRepository.findByPublicoTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApresentacaoDashboard> buscarPorId(@PathVariable Long id) {
        return dashboardRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        if (!dashboardRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dashboardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
