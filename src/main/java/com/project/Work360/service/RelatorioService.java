package com.project.Work360.service;

import com.project.Work360.dto.RelatorioResponse;
import com.project.Work360.mapper.RelatorioMapper;
import com.project.Work360.model.AnalyticsMetrica;
import com.project.Work360.model.Relatorio;
import com.project.Work360.model.Usuario;
import com.project.Work360.repository.AnalyticsMetricaRepository;
import com.project.Work360.repository.RelatorioRepository;
import com.project.Work360.repository.ReuniaoRepository;
import com.project.Work360.repository.TarefaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final RelatorioMapper relatorioMapper = new RelatorioMapper();

    @Autowired
    private AnalyticsMetricaRepository metricaRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ReuniaoRepository reuniaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    public RelatorioService(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }


    @Transactional
    public RelatorioResponse gerarRelatorioCompleto(Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        Usuario usuario = usuarioService.findUsuarioById(usuarioId);

        // Busca as métricas do intervalo
        List<AnalyticsMetrica> metricas = metricaRepository.findByUsuarioIdAndDataBetween(
                usuarioId, dataInicio, dataFim);

        if (metricas.isEmpty()) {
            System.out.println("Nenhuma métrica encontrada no período informado.");
            throw new RuntimeException("Sem métricas para gerar relatório.");
        }

        // ---- 1️⃣ Calcular somatórios e médias ----
        int totalTarefasConcluidas = metricas.stream()
                .mapToInt(m -> m.getTarefasConcluidasNoDia() != null ? m.getTarefasConcluidasNoDia() : 0)
                .sum();

        int totalMinutosFoco = metricas.stream()
                .mapToInt(m -> m.getMinutosFoco() != null ? m.getMinutosFoco() : 0)
                .sum();

        int totalMinutosReuniao = metricas.stream()
                .mapToInt(m -> m.getMinutosReuniao() != null ? m.getMinutosReuniao() : 0)
                .sum();

        // ---- 2️⃣ Buscar tarefas pendentes e reuniões realizadas ----
        int tarefasPendentes = (int) tarefaRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(t -> !t.isConcluida())
                .count();

        int reunioesRealizadas = (int) reuniaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(r -> r.getData() != null
                        && !r.getData().isBefore(dataInicio.atStartOfDay())
                        && !r.getData().isAfter(dataFim.atTime(23, 59)))
                .count();

        // ---- 3️⃣ Criar objeto de relatório consolidado ----
        Relatorio relatorio = new Relatorio();
        relatorio.setUsuario(usuario);
        relatorio.setDataInicio(dataInicio);
        relatorio.setDataFim(dataFim);
        relatorio.setTarefasConcluidas(totalTarefasConcluidas);
        relatorio.setTarefasPendentes(tarefasPendentes);
        relatorio.setReunioesRealizadas(reunioesRealizadas);
        relatorio.setMinutosFocoTotal(totalMinutosFoco);

        // Campos de IA (placeholder — preenchidos futuramente)
        relatorio.setPercentualConclusao(null);
        relatorio.setRiscoBurnout(null);
        relatorio.setTendenciaProdutividade(null);
        relatorio.setTendenciaFoco(null);

        // ---- 4️⃣ Salvar e retornar ----
        Relatorio salvo = relatorioRepository.save(relatorio);
        System.out.println("Relatório gerado com sucesso para o usuário: " + usuario.getNome());

        return relatorioMapper.toResponse(salvo);
    }

    public List<RelatorioResponse> findByUsuario(Long usuarioId) {
        return relatorioRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(relatorioMapper::toResponse)
                .toList();
    }

    public boolean deletarRelatorio(Long id) {
        if (relatorioRepository.existsById(id)) {
            relatorioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
