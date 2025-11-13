package com.project.Work360.service;

import com.project.Work360.dto.TarefaRequest;
import com.project.Work360.dto.TarefaResponse;
import com.project.Work360.mapper.TarefaMapper;
import com.project.Work360.model.Tarefa;
import com.project.Work360.model.Usuario;
import com.project.Work360.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioService usuarioService;
    private final TarefaMapper tarefaMapper = new TarefaMapper();

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository, UsuarioService usuarioService) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioService = usuarioService;
    }

    public TarefaResponse save(TarefaRequest request) {
        Usuario usuario = usuarioService.findUsuarioById(request.usuarioId());
        Tarefa tarefa = tarefaMapper.toEntity(request, usuario);
        Tarefa salva = tarefaRepository.save(tarefa);
        return tarefaMapper.toResponse(salva);
    }

    public Page<TarefaResponse> findAll(Pageable pageable) {
        return tarefaRepository.findAll(pageable).map(tarefaMapper::toResponse);
    }

    public TarefaResponse findById(Long id) {
        return tarefaRepository.findById(id).map(tarefaMapper::toResponse).orElse(null);
    }

    public Tarefa findTarefaById(Long id) {
        return tarefaRepository.findById(id).orElse(null);
    }

    public TarefaResponse update(Long id, TarefaRequest request) {
        Optional<Tarefa> optional = tarefaRepository.findById(id);
        if (optional.isPresent()) {
            Tarefa tarefa = optional.get();
            tarefa.setTitulo(request.titulo());
            tarefa.setDescricao(request.descricao());
            tarefa.setPrioridade(request.prioridade());
            tarefa.setEstimativaMinutos(request.estimativaMinutos());
            Tarefa atualizada = tarefaRepository.save(tarefa);
            return tarefaMapper.toResponse(atualizada);
        }
        return null;
    }

    public boolean delete(Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if (tarefa.isPresent()) {
            tarefaRepository.delete(tarefa.get());
            return true;
        }
        return false;
    }
}