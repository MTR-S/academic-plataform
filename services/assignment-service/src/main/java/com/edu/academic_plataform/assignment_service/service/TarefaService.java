package com.edu.academic_plataform.assignment_service.service;

import com.edu.academic_plataform.assignment_service.api.dto.TarefaRequestDTO;
import com.edu.academic_plataform.assignment_service.api.dto.TarefaResponseDTO;
import com.edu.academic_plataform.assignment_service.database.TarefaEntity;
import com.edu.academic_plataform.assignment_service.domain.exception.RecursoNaoEncontradoException;
import com.edu.academic_plataform.assignment_service.domain.model.Tarefa;
import com.edu.academic_plataform.assignment_service.persistence.mapper.TarefaMapper;
import com.edu.academic_plataform.assignment_service.persistence.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;

    public TarefaService(TarefaRepository tarefaRepository,
                         TarefaMapper tarefaMapper) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaMapper = tarefaMapper;
    }

    public TarefaResponseDTO salvar(TarefaRequestDTO tarefaDto, Principal principal) {

        Tarefa tarefa = tarefaMapper.toDomain(tarefaDto);
        tarefa.setCriadoPor(principal.getName());

        TarefaEntity tarefaEntity = tarefaMapper.toEntity(tarefa);

        TarefaEntity tarefaEntitySalva = tarefaRepository.save(tarefaEntity);

        return tarefaMapper.toDto(tarefaEntitySalva);
    }

    public List<TarefaResponseDTO> listarTodas() {
        List<TarefaEntity> entidades = tarefaRepository.findAll();

        return entidades.stream()
                .map(tarefaMapper::toDto)
                .toList();
    }

    public void deletar(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Tarefa não encontrada com o ID: " + id);
        }

        tarefaRepository.deleteById(id);
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        TarefaEntity entidadeExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com o ID: " + id));

        tarefaMapper.atualizarEntidadeDoDto(dto, entidadeExistente);

        TarefaEntity entidadeSalva = tarefaRepository.save(entidadeExistente);

        return tarefaMapper.toDto(entidadeSalva);
    }
}