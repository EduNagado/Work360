package com.project.Work360.ai.controller;


import com.project.Work360.ai.orchestrator.IAOrquestradoraService;
import com.project.Work360.dto.RelatorioResponse;
import com.project.Work360.mapper.RelatorioMapper;
import com.project.Work360.model.Relatorio;
import com.project.Work360.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
@Tag(name = "7 - IA")
public class IAController {

    @Autowired
    private IAOrquestradoraService orquestradoraService;

    @Autowired
    private RelatorioService relatorioService;

    private final RelatorioMapper mapper = new RelatorioMapper();

    @Operation(
            summary = "Executa análises inteligentes sobre um relatório",
            description = "Processa o relatório informado utilizando os módulos de IA, gerando classificações, insights e métricas avançadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório processado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RelatorioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Relatório não encontrado!", content = @Content)
    })
    @GetMapping("/relatorio/{id}")
    public ResponseEntity<RelatorioResponse> processar(@PathVariable Long id) {

        Relatorio relatorio = relatorioService.findEntityById(id);

        if (relatorio == null) {
            return ResponseEntity.notFound().build();
        }

        Relatorio processado = orquestradoraService.processarRelatorio(relatorio);

        return ResponseEntity.ok(mapper.toResponse(processado));
    }
}