package com.project.Work360.controller;

import com.project.Work360.dto.AnalyticsEventoRequest;
import com.project.Work360.dto.AnalyticsEventoResponse;
import com.project.Work360.dto.AnalyticsMetricaResponse;
import com.project.Work360.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@Tag(name = "5 - Analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Operation(summary = "Cria um novo evento analítico e atualiza as métricas automaticamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado e métricas atualizadas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsEventoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao criar evento !", content = @Content)
    })
    @PostMapping("/eventos")
    public ResponseEntity<AnalyticsEventoResponse> createEvento(@Valid @RequestBody AnalyticsEventoRequest request) {
        AnalyticsEventoResponse response = analyticsService.saveEvento(request);


        analyticsService.gerarMetricasCompletas(request.usuarioId());

        return ResponseEntity.status(201).body(response);
    }


    @Operation(summary = "Lista o histórico completo de métricas de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsMetricaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma métrica encontrada!", content = @Content)
    })
    @GetMapping("/metricas/{usuarioId}")
    public ResponseEntity<List<AnalyticsMetricaResponse>> findMetricas(@PathVariable Long usuarioId) {
        List<AnalyticsMetricaResponse> metricas = analyticsService.findMetricasByUsuario(usuarioId);

        if (metricas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(metricas);
    }


    @Operation(summary = "Retorna as métricas do dia atual para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas do dia retornadas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsMetricaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma métrica encontrada para hoje!", content = @Content)
    })
    @GetMapping("/metricas/{usuarioId}/hoje")
    public ResponseEntity<AnalyticsMetricaResponse> findMetricasDoDia(@PathVariable Long usuarioId) {
        LocalDate hoje = LocalDate.now();
        return analyticsService.findMetricaDoDia(usuarioId, hoje)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
